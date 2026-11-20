package com.appmattus.markdown.processing

import com.appmattus.markdown.errors.Error
import java.nio.file.Path

internal object ReportGenerator {

    fun checkstyleXml(fileErrors: Map<Path, List<Error>>): String {
        val files = fileErrors.entries.joinToString("\n") { (file, errors) ->
            val formattedErrors = errors.joinToString("\n") { error ->
                """
                <error line="${error.lineNumber}" column="${error.columnNumber}" severity="error"
                  message="${error.errorMessage.escapeXml()}" source="${error.ruleClass.name.escapeXml()}"/>
                """.trimIndent().prependIndent("    ")
            }

            """  <file name="${file.toString().escapeXml()}">
$formattedErrors
  </file>"""
        }

        return """<?xml version="1.0" encoding="UTF-8"?>
<checkstyle version="8.18">
$files
</checkstyle>
"""
    }

    fun html(fileErrors: Map<Path, List<Error>>): String {
        val files = fileErrors.entries.sortedWith(
            compareByDescending<Map.Entry<Path, List<Error>>> { it.value.size }
                .thenBy { it.key.toString() }
        )
        return htmlPage(
            fileCount = fileErrors.size,
            errorCount = fileErrors.values.sumOf { it.size },
            fileList = files.htmlFileList(),
            violations = files.htmlViolations()
        )
    }

    private fun List<Map.Entry<Path, List<Error>>>.htmlFileList(): String =
        mapIndexed { index, (file, errors) ->
            """
            <tr>
              <td><a href="#file-${index + 1}">${file.toString().escapeHtml()}</a></td>
              <td>${errors.size}</td>
            </tr>
            """.trimIndent().prependIndent("          ")
        }.joinToString("\n")

    private fun List<Map.Entry<Path, List<Error>>>.htmlViolations(): String =
        mapIndexed { index, (file, errors) ->
            val rows = errors.sortedWith(compareBy<Error> { it.lineNumber }.thenBy { it.columnNumber })
                .joinToString("\n") { error ->
                    """
                    <tr>
                      <td>${error.errorMessage.escapeHtml()}</td>
                      <td>${error.lineNumber}</td>
                    </tr>
                    """.trimIndent().prependIndent("          ")
                }

            """
            <section class="file-violations" id="file-${index + 1}">
              <h2>${file.toString().escapeHtml()}</h2>
              <table>
                <thead>
                  <tr><th>Error Description</th><th>Line</th></tr>
                </thead>
                <tbody>
            $rows
                </tbody>
              </table>
              <a href="#top">Back to top</a>
            </section>
            """.trimIndent().prependIndent("        ")
        }.joinToString("\n")

    private fun htmlPage(
        fileCount: Int,
        errorCount: Int,
        fileList: String,
        violations: String
    ): String =
        """
            <!DOCTYPE html>
            <html lang="en">
            <head>
              <meta charset="UTF-8"/>
              <title>Markdown Lint Report</title>
              <style>
                body { color: #1f2328; font: 14px Arial, sans-serif; margin: 2rem; }
                a { color: #0969da; }
                table { border-collapse: collapse; margin-bottom: 1.5rem; width: 100%; }
                th, td { border: 1px solid #d0d7de; padding: 0.5rem; text-align: left; }
                th { background: #f6f8fa; }
                .summary { width: auto; }
                .file-violations { margin-top: 2rem; }
              </style>
            </head>
            <body>
              <main id="top">
                <h1>Markdown Lint Report</h1>
                <h2>Summary</h2>
                <table class="summary">
                  <thead>
                    <tr><th>Files</th><th>Errors</th></tr>
                  </thead>
                  <tbody>
                    <tr><td>$fileCount</td><td>$errorCount</td></tr>
                  </tbody>
                </table>
                <h2>Files</h2>
                <table>
                  <thead>
                    <tr><th>Name</th><th>Errors</th></tr>
                  </thead>
                  <tbody>
            $fileList
                  </tbody>
                </table>
            $violations
              </main>
            </body>
            </html>
        """.trimIndent() + "\n"

    private fun String.escapeXml(): String = escapeMarkup(apos = "&apos;")

    private fun String.escapeHtml(): String = escapeMarkup(apos = "&#39;")

    private fun String.escapeMarkup(apos: String): String = buildString {
        var index = 0
        while (index < this@escapeMarkup.length) {
            val codePoint = this@escapeMarkup.codePointAt(index)
            when (codePoint) {
                '&'.code -> append("&amp;")
                '<'.code -> append("&lt;")
                '>'.code -> append("&gt;")
                '"'.code -> append("&quot;")
                '\''.code -> append(apos)
                '\n'.code -> append("&#10;")
                '\r'.code -> append("&#13;")
                else -> {
                    if (codePoint.isValidXmlCodePoint()) {
                        appendCodePoint(codePoint)
                    } else {
                        append('\uFFFD')
                    }
                }
            }
            index += Character.charCount(codePoint)
        }
    }

    private fun Int.isValidXmlCodePoint(): Boolean =
        this == '\t'.code ||
            this in MIN_XML_CHARACTER..MAX_XML_CHARACTER_BEFORE_SURROGATES ||
            this in MIN_XML_CHARACTER_AFTER_SURROGATES..MAX_BASIC_XML_CHARACTER ||
            this in MIN_SUPPLEMENTARY_XML_CHARACTER..MAX_XML_CHARACTER

    private const val MIN_XML_CHARACTER = 0x20
    private const val MAX_XML_CHARACTER_BEFORE_SURROGATES = 0xD7FF
    private const val MIN_XML_CHARACTER_AFTER_SURROGATES = 0xE000
    private const val MAX_BASIC_XML_CHARACTER = 0xFFFD
    private const val MIN_SUPPLEMENTARY_XML_CHARACTER = 0x10000
    private const val MAX_XML_CHARACTER = 0x10FFFF
}

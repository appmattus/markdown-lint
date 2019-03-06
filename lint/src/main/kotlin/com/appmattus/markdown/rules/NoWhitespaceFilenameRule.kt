package com.appmattus.markdown.rules

import com.appmattus.markdown.ErrorReporter
import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup

/**
 * # Replace white space characters by hyphens
 *
 * Prefer to base the file name on the top-header level:
 * 1. replace upper case letters with lower case
 * 1. strip articles the, a, an from the start
 * 1. replace punctuation and white space characters by hyphens
 * 1. replace consecutive hyphens by a single hyphen
 * 1. strip surrounding hyphens
 *
 * Good:
 *
 *     file-name.md
 *
 * Bad, multiple consecutive hyphens:
 *
 *     file--name.md
 *
 * Bad, surrounding hyphens:
 *
 *     -file-name-.md
 *
 * Rationale: why not underscore or camel case? Hyphens are the most popular URL separator today, and markdown files
 * are most often used in contexts where:
 * 1. there are hyphen separated HTML files in the same project, possibly the same directory as the markdown files.
 * 1. filenames will be used directly on URLs. E.g.: GitHub blobs.
 *
 * Based on [File name](https://www.cirosantilli.com/markdown-style-guide/#file-name)
 */
class NoWhitespaceFilenameRule(
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    override val description = "Filenames must not contain whitespace"
    override val tags = listOf("file_name")

    private val whitespace = "\\s".toRegex()

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        if (document.filename.contains(whitespace)) {
            errorReporter.reportError(0, 0, description)
        }
    }
}

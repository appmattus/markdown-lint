package com.appmattus.markdown.processing

import com.appmattus.markdown.errors.Error
import com.appmattus.markdown.rules.Rule
import com.appmattus.markdown.rules.SingleH1Rule
import org.assertj.core.api.Assertions.assertThat
import java.nio.file.Paths
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.test.Test

class ReportGeneratorTest {

    @Test
    fun `checkstyle report is compatible XML and escapes attributes`() {
        val report = ReportGenerator.checkstyleXml(
            linkedMapOf(
                Paths.get("docs/a&b\"'.md") to listOf(
                    error(message = "Use <tag> & \"quotes\", 'apostrophes' and 😺\non the next line")
                )
            )
        )

        val document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(report.byteInputStream())
        val file = document.getElementsByTagName("file").item(0)
        val finding = document.getElementsByTagName("error").item(0)

        assertThat(document.documentElement.tagName).isEqualTo("checkstyle")
        assertThat(document.documentElement.attributes.getNamedItem("version").nodeValue).isEqualTo("8.18")
        assertThat(file.attributes.getNamedItem("name").nodeValue).isEqualTo("docs/a&b\"'.md")
        assertThat(finding.attributes.getNamedItem("line").nodeValue).isEqualTo("3")
        assertThat(finding.attributes.getNamedItem("column").nodeValue).isEqualTo("5")
        assertThat(finding.attributes.getNamedItem("severity").nodeValue).isEqualTo("error")
        assertThat(finding.attributes.getNamedItem("message").nodeValue)
            .isEqualTo("Use <tag> & \"quotes\", 'apostrophes' and 😺\non the next line")
        assertThat(finding.attributes.getNamedItem("source").nodeValue).isEqualTo(SingleH1Rule::class.java.name)
    }

    @Test
    fun `html report contains summary files and escaped violations`() {
        val report = ReportGenerator.html(
            linkedMapOf(
                Paths.get("docs/a&b.md") to listOf(
                    error(message = "Use <tag> & \"quotes\"")
                ),
                Paths.get("docs/more-errors.md") to listOf(
                    error(line = 8, message = "Second"),
                    error(line = 2, message = "First")
                )
            )
        )

        val document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(report.byteInputStream())

        assertThat(document.documentElement.tagName).isEqualTo("html")
        assertThat(report).contains("<title>Markdown Lint Report</title>")
        assertThat(report).contains("<tr><td>2</td><td>3</td></tr>")
        assertThat(report).contains("docs/a&amp;b.md")
        assertThat(report).contains("Use &lt;tag&gt; &amp; &quot;quotes&quot;")
        assertThat(report.indexOf("docs/more-errors.md")).isLessThan(report.indexOf("docs/a&amp;b.md"))
        assertThat(report.indexOf("First")).isLessThan(report.indexOf("Second"))
    }

    @Suppress("UNCHECKED_CAST")
    private fun error(
        line: Int = 3,
        column: Int = 5,
        message: String
    ) = Error(
        startOffset = 0,
        endOffset = 1,
        lineNumber = line,
        columnNumber = column,
        errorMessage = message,
        ruleClass = SingleH1Rule::class.java as Class<Rule>
    )
}

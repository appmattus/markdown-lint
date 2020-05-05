package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory

class LineLengthRuleTest {

    @TestFactory
    fun lineLengthRule() = FileTestFactory(
        allFiles + "long_lines_inline_code.md" + "long_lines_image.md" + "long_lines_close.md",
        exclude = listOf("long_lines_100.md", "long_lines_code.md")
    ) { LineLengthRule() }

    @TestFactory
    fun `lineLengthRule with line length`() =
        FileTestFactory(listOf("long_lines_100.md")) { LineLengthRule(lineLength = 100) }

    @TestFactory
    fun `lineLengthRule code blocks and tables disabled`() =
        FileTestFactory(listOf("long_lines_code.md")) { LineLengthRule(codeBlocks = false, tables = false) }

    @TestFactory
    fun `lineLengthRule headings disabled`() =
        FileTestFactory(listOf("long_lines_heading_exceptions.md")) { LineLengthRule(headings = false) }
}

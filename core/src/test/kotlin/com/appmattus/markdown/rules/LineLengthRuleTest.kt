package com.appmattus.markdown.rules

import kotlin.test.Test

class LineLengthRuleTest {
    @Test
    fun `long_lines_inline_code`() = fileRuleScenario(listOf("long_lines_inline_code.md")) { LineLengthRule() }

    @Test
    fun `long_lines_image`() = fileRuleScenario(listOf("long_lines_image.md")) { LineLengthRule() }

    @Test
    fun `long_lines_emphasis`() = fileRuleScenario(listOf("long_lines_emphasis.md")) { LineLengthRule() }

    @Test
    fun `long_lines_close`() = fileRuleScenario(listOf("long_lines_close.md")) { LineLengthRule() }

    @Test
    fun `long_lines_100`() = fileRuleScenario(listOf("long_lines_100.md")) { LineLengthRule(lineLength = 100) }

    @Test
    fun `long_lines_code`() = fileRuleScenario(
        listOf("long_lines_code.md")
    ) { LineLengthRule(codeBlocks = false, tables = false) }

    @Test
    fun `long_lines_heading_exceptions`() = fileRuleScenario(
        listOf("long_lines_heading_exceptions.md")
    ) { LineLengthRule(headings = false) }

    @Test
    fun `all files except long_lines_100, long_lines_code`() = fileRuleScenario(
        exclude = listOf("long_lines_100.md", "long_lines_code.md")
    ) { LineLengthRule() }
}

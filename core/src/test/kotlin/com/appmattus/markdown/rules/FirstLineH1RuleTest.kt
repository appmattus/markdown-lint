package com.appmattus.markdown.rules

import kotlin.test.Test

class FirstLineH1RuleTest {
    @Test
    fun `alternate_top_level_header`() = fileRuleScenario(listOf("alternate_top_level_header.md")) {
        FirstLineH1Rule(level = 2)
    }

    @Test
    fun `first_line_top_level_header_atx, first_line_top_level_header_setext, header_trailing_punctuation_customized, headers_good_setext_with_atx, incorrect_header_atx_closed, incorrect_header_setext, no_first_line_header, no_first_line_top_level_header`() = fileRuleScenario(
        listOf(
            "first_line_top_level_header_atx.md",
            "first_line_top_level_header_setext.md",

            "header_trailing_punctuation_customized.md",
            "headers_good_setext_with_atx.md",
            "incorrect_header_atx_closed.md",
            "incorrect_header_setext.md",
            "no_first_line_header.md",
            "no_first_line_top_level_header.md"

        )
    ) { FirstLineH1Rule() }
}

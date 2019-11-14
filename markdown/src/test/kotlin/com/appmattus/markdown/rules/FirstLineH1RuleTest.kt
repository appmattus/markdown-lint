package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object FirstLineH1RuleTest : Spek({
    Feature("FirstLineH1Rule") {
        FileRuleScenario(listOf("alternate_top_level_header.md")) { FirstLineH1Rule(level = 2) }

        FileRuleScenario(
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
})

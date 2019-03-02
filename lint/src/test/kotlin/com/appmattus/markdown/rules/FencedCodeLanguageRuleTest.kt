package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object FencedCodeLanguageRuleTest : Spek({
    Feature("FencedCodeLanguageRule") {
        FileRuleScenario(
            exclude = listOf(
                "code_block_consistency.md",
                "code_block_fenced.md",
                "code_block_indented.md",
                "fenced_code_without_blank_lines.md"
            )
        ) { FencedCodeLanguageRule() }
    }
})

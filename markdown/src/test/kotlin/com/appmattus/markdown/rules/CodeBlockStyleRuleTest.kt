package com.appmattus.markdown.rules

import com.appmattus.markdown.rules.config.CodeBlockStyle
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object CodeBlockStyleRuleTest : Spek({
    Feature("CodeBlockStyleRule") {
        FileRuleScenario(listOf("code_block_consistency.md")) { CodeBlockStyleRule(style = CodeBlockStyle.Consistent) }

        FileRuleScenario(listOf("code_block_fenced.md")) { CodeBlockStyleRule(style = CodeBlockStyle.Fenced) }

        FileRuleScenario(listOf("code_block_indented.md")) { CodeBlockStyleRule(style = CodeBlockStyle.Indented) }
    }
})

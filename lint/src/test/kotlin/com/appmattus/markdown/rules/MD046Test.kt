package com.appmattus.markdown.rules

import com.appmattus.markdown.rules.config.CodeBlockStyle
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object MD046Test : Spek({
    Feature("MD046") {
        FileRuleScenario(listOf("code_block_consistency.md")) { MD046(style = CodeBlockStyle.Consistent) }

        FileRuleScenario(listOf("code_block_fenced.md")) { MD046(style = CodeBlockStyle.Fenced) }

        FileRuleScenario(listOf("code_block_indented.md")) { MD046(style = CodeBlockStyle.Indented) }
    }
})

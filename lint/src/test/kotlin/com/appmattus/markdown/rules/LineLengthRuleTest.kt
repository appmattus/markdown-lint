package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object LineLengthRuleTest : Spek({
    Feature("LineLengthRule") {
        FileRuleScenario(listOf("long_lines_100.md")) { LineLengthRule(lineLength = 100) }

        FileRuleScenario(listOf("long_lines_code.md")) { LineLengthRule(codeBlocks = false, tables = false) }

        FileRuleScenario(exclude = listOf("long_lines_100.md", "long_lines_code.md")) { LineLengthRule() }
    }
})

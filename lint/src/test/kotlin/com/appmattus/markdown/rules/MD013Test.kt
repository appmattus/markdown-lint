package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object MD013Test : Spek({
    Feature("MD013") {
        FileRuleScenario(listOf("long_lines_100.md")) { MD013(lineLength = 100) }

        FileRuleScenario(listOf("long_lines_code.md")) { MD013(codeBlocks = false, tables = false) }

        FileRuleScenario(exclude = listOf("long_lines_100.md", "long_lines_code.md")) { MD013() }
    }
})

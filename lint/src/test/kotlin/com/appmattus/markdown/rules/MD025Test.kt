package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object MD025Test : Spek({
    Feature("MD025") {
        FileRuleScenario(listOf("alternate_top_level_header.md")) { MD025(level = 2) }

        FileRuleScenario(exclude = listOf("alternate_top_level_header.md")) { MD025() }
    }
})

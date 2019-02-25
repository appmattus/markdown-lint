package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object MD002Test : Spek({
    Feature("MD002") {
        FileRuleScenario(listOf("alternate_top_level_header.md")) { MD002(level = 2) }

        FileRuleScenario(exclude = listOf("alternate_top_level_header.md")) { MD002() }
    }
})

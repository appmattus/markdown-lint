package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object MD002Test : Spek({
    Feature("FirstHeaderH1Rule") {
        FileRuleScenario(listOf("alternate_top_level_header.md")) { FirstHeaderH1Rule(level = 2) }

        FileRuleScenario(exclude = listOf("alternate_top_level_header.md")) { FirstHeaderH1Rule() }
    }
})

package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object MD042Test : Spek({
    Feature("MD042") {
        FileRuleScenario(exclude = listOf("reversed_link.md")) { MD042() }
    }
})

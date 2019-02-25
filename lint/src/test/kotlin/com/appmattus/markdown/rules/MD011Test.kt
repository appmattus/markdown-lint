package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object MD011Test : Spek({
    Feature("MD011") {
        FileRuleScenario(exclude = listOf("empty-links.md")) { MD011() }
    }
})

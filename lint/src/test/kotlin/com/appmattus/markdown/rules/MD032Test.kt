package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object MD032Test : Spek({
    Feature("MD032") {
        FileRuleScenario(exclude = listOf("break-all-the-rules.md")) { MD032() }
    }
})

package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object MD019Test : Spek({
    Feature("MD019") {
        FileRuleScenario { MD019() }
    }
})

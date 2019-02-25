package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object MD010Test : Spek({
    Feature("MD010") {
        FileRuleScenario { MD010() }
    }
})

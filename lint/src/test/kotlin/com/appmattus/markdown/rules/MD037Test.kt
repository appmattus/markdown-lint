package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object MD037Test : Spek({
    Feature("MD037") {
        FileRuleScenario { MD037() }
    }
})

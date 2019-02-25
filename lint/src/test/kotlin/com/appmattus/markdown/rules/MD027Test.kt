package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object MD027Test : Spek({
    Feature("MD027") {
        FileRuleScenario { MD027() }
    }
})

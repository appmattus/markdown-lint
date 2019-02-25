package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object MD001Test : Spek({
    Feature("MD001") {
        FileRuleScenario { MD001() }
    }
})

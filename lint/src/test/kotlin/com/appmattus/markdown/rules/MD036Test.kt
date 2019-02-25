package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object MD036Test : Spek({
    Feature("MD036") {
        FileRuleScenario { MD036() }
    }
})

package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object MD038Test : Spek({
    Feature("MD038") {
        FileRuleScenario { MD038() }
    }
})

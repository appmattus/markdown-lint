package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object MD005Test : Spek({
    Feature("MD005") {
        FileRuleScenario { MD005() }
    }
})

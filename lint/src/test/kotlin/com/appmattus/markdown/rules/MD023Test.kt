package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object MD023Test : Spek({
    Feature("MD023") {
        FileRuleScenario { MD023() }
    }
})

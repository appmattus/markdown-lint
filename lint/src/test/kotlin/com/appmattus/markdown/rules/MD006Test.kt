package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object MD006Test : Spek({
    Feature("MD006") {
        FileRuleScenario { MD006() }
    }
})

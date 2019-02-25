package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object MD018Test : Spek({
    Feature("MD018") {
        FileRuleScenario { MD018() }
    }
})

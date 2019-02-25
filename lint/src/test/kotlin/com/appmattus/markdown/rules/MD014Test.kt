package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object MD014Test : Spek({
    Feature("MD014") {
        FileRuleScenario { MD014() }
    }
})

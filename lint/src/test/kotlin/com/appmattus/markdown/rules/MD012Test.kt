package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object MD012Test : Spek({
    Feature("MD012") {
        FileRuleScenario { MD012() }
    }
})

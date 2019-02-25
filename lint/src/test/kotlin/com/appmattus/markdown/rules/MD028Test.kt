package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object MD028Test : Spek({
    Feature("MD028") {
        FileRuleScenario { MD028() }
    }
})

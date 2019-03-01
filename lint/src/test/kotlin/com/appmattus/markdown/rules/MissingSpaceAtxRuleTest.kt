package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object MissingSpaceAtxRuleTest : Spek({
    Feature("NoMissingSpaceAtxRule") {
        FileRuleScenario { NoMissingSpaceAtxRule() }
    }
})

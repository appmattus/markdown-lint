package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object BlanksAroundFencesRuleTest : Spek({
    Feature("BlanksAroundFencesRule") {
        FileRuleScenario { BlanksAroundFencesRule() }
    }
})

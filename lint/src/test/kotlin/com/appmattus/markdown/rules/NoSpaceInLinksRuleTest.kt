package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object NoSpaceInLinksRuleTest : Spek({
    Feature("NoSpaceInLinksRule") {
        FileRuleScenario { NoSpaceInLinksRule() }
    }
})

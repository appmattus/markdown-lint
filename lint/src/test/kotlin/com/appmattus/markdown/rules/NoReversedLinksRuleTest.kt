package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object NoReversedLinksRuleTest : Spek({
    Feature("NoReversedLinksRule") {
        FileRuleScenario(exclude = listOf("empty-links.md")) { NoReversedLinksRule() }
    }
})

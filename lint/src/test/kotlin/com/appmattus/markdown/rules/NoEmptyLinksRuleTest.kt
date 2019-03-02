package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object NoEmptyLinksRuleTest : Spek({
    Feature("NoEmptyLinksRule") {
        FileRuleScenario(exclude = listOf("reversed_link.md")) { NoEmptyLinksRule() }
    }
})

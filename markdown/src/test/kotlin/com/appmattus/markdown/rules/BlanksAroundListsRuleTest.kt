package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object BlanksAroundListsRuleTest : Spek({
    Feature("BlanksAroundListsRule") {
        FileRuleScenario(exclude = listOf("break-all-the-rules.md")) { BlanksAroundListsRule() }
    }
})

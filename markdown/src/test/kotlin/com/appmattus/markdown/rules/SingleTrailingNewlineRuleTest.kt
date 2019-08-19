package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object SingleTrailingNewlineRuleTest : Spek({
    Feature("SingleTrailingNewlineRule") {
        FileRuleScenario(listOf("no_newline.md")) { SingleTrailingNewlineRule() }

        FileRuleScenario(listOf("has_newline.md")) { SingleTrailingNewlineRule() }
    }
})

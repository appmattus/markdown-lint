package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object NoTrailingPunctuationRuleTest : Spek({
    Feature("NoTrailingPunctuationRule") {
        FileRuleScenario(listOf("header_trailing_punctuation_customized.md")) { NoTrailingPunctuationRule(punctuation = ".,;:!") }

        FileRuleScenario(exclude = listOf("header_trailing_punctuation_customized.md")) { NoTrailingPunctuationRule() }
    }
})

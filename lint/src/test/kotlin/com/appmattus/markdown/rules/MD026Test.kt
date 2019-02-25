package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object MD026Test : Spek({
    Feature("MD026") {
        FileRuleScenario(listOf("header_trailing_punctuation_customized.md")) { MD026(punctuation = ".,;:!") }

        FileRuleScenario(exclude = listOf("header_trailing_punctuation_customized.md")) { MD026() }
    }
})

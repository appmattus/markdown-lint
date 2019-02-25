package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object MD024Test : Spek({
    Feature("MD024") {
        FileRuleScenario(listOf("header_duplicate_content_different_nesting.md")) { MD024(allowDifferentNesting = true) }

        FileRuleScenario(exclude = listOf("header_duplicate_content_different_nesting.md")) { MD024() }
    }
})

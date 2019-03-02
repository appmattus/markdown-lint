package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object NoDuplicateHeaderRuleTest : Spek({
    Feature("NoDuplicateHeaderRule") {
        FileRuleScenario(listOf("header_duplicate_content_different_nesting.md")) { NoDuplicateHeaderRule(allowDifferentNesting = true) }

        FileRuleScenario(exclude = listOf("header_duplicate_content_different_nesting.md")) { NoDuplicateHeaderRule() }
    }
})

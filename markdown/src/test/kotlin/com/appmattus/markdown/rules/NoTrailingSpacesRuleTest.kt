package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object NoTrailingSpacesRuleTest : Spek({
    Feature("NoTrailingSpacesRule") {
        FileRuleScenario(listOf("trailing_spaces_br.md")) { NoTrailingSpacesRule(brSpaces = 2) }

        FileRuleScenario(exclude = listOf("trailing_spaces_br.md")) { NoTrailingSpacesRule() }
    }
})

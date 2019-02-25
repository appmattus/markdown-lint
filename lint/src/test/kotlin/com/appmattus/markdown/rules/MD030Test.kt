package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object MD030Test : Spek({
    Feature("MD030") {
        FileRuleScenario(listOf("spaces_after_list_marker.md")) { MD030(ulMulti = 3, olMulti = 2) }

        FileRuleScenario(exclude = listOf("spaces_after_list_marker.md")) { MD030() }
    }
})

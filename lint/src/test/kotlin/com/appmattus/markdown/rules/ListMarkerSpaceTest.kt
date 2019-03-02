package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object ListMarkerSpaceTest : Spek({
    Feature("ListMarkerSpaceRule") {
        FileRuleScenario(listOf("spaces_after_list_marker.md")) { ListMarkerSpaceRule(ulMulti = 3, olMulti = 2) }

        FileRuleScenario(exclude = listOf("spaces_after_list_marker.md")) { ListMarkerSpaceRule() }
    }
})

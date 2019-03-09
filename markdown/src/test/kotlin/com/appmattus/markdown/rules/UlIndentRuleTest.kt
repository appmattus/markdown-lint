package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object UlIndentRuleTest : Spek({
    Feature("UlIndentRule") {
        FileRuleScenario(listOf("bulleted_list_2_space_indent.md")) { UlIndentRule(indent = 4) }

        FileRuleScenario(listOf("spaces_after_list_marker.md")) { UlIndentRule(indent = 4) }

        FileRuleScenario(
            exclude = listOf(
                "bulleted_list_2_space_indent.md",
                "spaces_after_list_marker.md",
                "bulleted_list_not_at_beginning_of_line.md"
            )
        ) { UlIndentRule() }
    }
})

package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object TaskListMarkerSpaceRuleTest : Spek({
    Feature("TaskListMarkerSpaceRule") {
        FileRuleScenario(listOf("task-list-marker.md")) { TaskListMarkerSpaceRule() }

        FileRuleScenario(listOf("task-list-marker-large-indent.md")) { TaskListMarkerSpaceRule(indent = 2) }

        FileRuleScenario { TaskListMarkerSpaceRule() }
    }
})

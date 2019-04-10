package com.appmattus.markdown.rules

import com.appmattus.markdown.rules.config.TaskListItemMarkerStyle
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object ConsistentTaskListMarkerStyleRuleTest : Spek({
    Feature("ConsistentTaskListMarkerStyleRule") {

        FileRuleScenario(listOf("task-list-marker.md")) {
            ConsistentTaskListMarkerStyleRule(TaskListItemMarkerStyle.Consistent)
        }

        FileRuleScenario(listOf("task-list-marker.md")) {
            ConsistentTaskListMarkerStyleRule(TaskListItemMarkerStyle.Lowercase)
        }

        FileRuleScenario(listOf("task-list-marker-uppercase.md")) {
            ConsistentTaskListMarkerStyleRule(TaskListItemMarkerStyle.Uppercase)
        }

        FileRuleScenario(listOf("task-list-marker-no-complete.md")) {
            ConsistentTaskListMarkerStyleRule(TaskListItemMarkerStyle.Consistent)
        }

        FileRuleScenario { ConsistentTaskListMarkerStyleRule() }
    }
})

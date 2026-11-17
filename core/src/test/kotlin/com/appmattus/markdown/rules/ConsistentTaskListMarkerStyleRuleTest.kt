package com.appmattus.markdown.rules

import com.appmattus.markdown.rules.config.TaskListItemMarkerStyle
import kotlin.test.Test

class ConsistentTaskListMarkerStyleRuleTest {
    @Test
    fun `task-list-marker with Consistent`() = fileRuleScenario(listOf("task-list-marker.md")) {
        ConsistentTaskListMarkerStyleRule(TaskListItemMarkerStyle.Consistent)
    }

    @Test
    fun `task-list-marker with Lowercase`() = fileRuleScenario(listOf("task-list-marker.md")) {
        ConsistentTaskListMarkerStyleRule(TaskListItemMarkerStyle.Lowercase)
    }

    @Test
    fun `task-list-marker-uppercase`() = fileRuleScenario(listOf("task-list-marker-uppercase.md")) {
        ConsistentTaskListMarkerStyleRule(TaskListItemMarkerStyle.Uppercase)
    }

    @Test
    fun `task-list-marker-no-complete`() = fileRuleScenario(listOf("task-list-marker-no-complete.md")) {
        ConsistentTaskListMarkerStyleRule(TaskListItemMarkerStyle.Consistent)
    }

    @Test
    fun `all files`() = fileRuleScenario { ConsistentTaskListMarkerStyleRule() }
}

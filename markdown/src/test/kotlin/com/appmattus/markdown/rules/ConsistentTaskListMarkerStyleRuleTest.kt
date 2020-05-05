package com.appmattus.markdown.rules

import com.appmattus.markdown.rules.config.TaskListItemMarkerStyle
import org.junit.jupiter.api.TestFactory

class ConsistentTaskListMarkerStyleRuleTest {

    @TestFactory
    fun `consistentTaskListMarkerStyleRule consistent`() =
        FileTestFactory(listOf("task-list-marker.md", "task-list-marker-no-complete.md")) {
            ConsistentTaskListMarkerStyleRule(TaskListItemMarkerStyle.Consistent)
        }

    @TestFactory
    fun `consistentTaskListMarkerStyleRule lowercase`() =
        FileTestFactory(listOf("task-list-marker.md")) {
            ConsistentTaskListMarkerStyleRule(TaskListItemMarkerStyle.Lowercase)
        }

    @TestFactory
    fun `consistentTaskListMarkerStyleRule uppercase`() =
        FileTestFactory(listOf("task-list-marker-uppercase.md")) {
            ConsistentTaskListMarkerStyleRule(TaskListItemMarkerStyle.Uppercase)
        }

    @TestFactory
    fun consistentTaskListMarkerStyleRule() = FileTestFactory { ConsistentTaskListMarkerStyleRule() }
}

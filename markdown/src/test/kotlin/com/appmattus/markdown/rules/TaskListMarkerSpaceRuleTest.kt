package com.appmattus.markdown.rules

import kotlin.test.Test

class TaskListMarkerSpaceRuleTest {
    @Test
    fun `task-list-marker`() = fileRuleScenario(listOf("task-list-marker.md")) { TaskListMarkerSpaceRule() }

    @Test
    fun `task-list-marker-large-indent`() = fileRuleScenario(listOf("task-list-marker-large-indent.md")) {
        TaskListMarkerSpaceRule(indent = 2)
    }

    @Test
    fun `all files`() = fileRuleScenario { TaskListMarkerSpaceRule() }
}

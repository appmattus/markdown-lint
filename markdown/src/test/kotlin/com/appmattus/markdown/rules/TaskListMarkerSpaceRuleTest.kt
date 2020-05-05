package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory

class TaskListMarkerSpaceRuleTest {

    @TestFactory
    fun taskListMarkerSpaceRule() = FileTestFactory(allFiles + "task-list-marker.md") { TaskListMarkerSpaceRule() }

    @TestFactory
    fun `taskListMarkerSpaceRule indent 2`() =
        FileTestFactory(listOf("task-list-marker-large-indent.md")) { TaskListMarkerSpaceRule(indent = 2) }
}

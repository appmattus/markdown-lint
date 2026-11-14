package com.appmattus.markdown.rules

import kotlin.test.Test

class ListMarkerSpaceRuleTest {
    @Test
    fun `task-list-marker`() = fileRuleScenario(listOf("task-list-marker.md")) { ListMarkerSpaceRule() }

    @Test
    fun `spaces_after_list_marker`() = fileRuleScenario(listOf("spaces_after_list_marker.md")) {
        ListMarkerSpaceRule(ulMulti = 3, olMulti = 2)
    }

    @Test
    fun `all files except spaces_after_list_marker`() = fileRuleScenario(
        exclude = listOf("spaces_after_list_marker.md")
    ) {
        ListMarkerSpaceRule()
    }
}

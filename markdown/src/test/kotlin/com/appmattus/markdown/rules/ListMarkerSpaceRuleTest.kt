package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory

class ListMarkerSpaceRuleTest {

    @TestFactory
    fun listMarkerSpaceRule() = FileTestFactory(
        allFiles + "task-list-marker.md",
        exclude = listOf("spaces_after_list_marker.md")
    ) { ListMarkerSpaceRule() }

    @TestFactory
    fun `listMarkerSpaceRule with overrides`() =
        FileTestFactory(listOf("spaces_after_list_marker.md")) { ListMarkerSpaceRule(ulMulti = 3, olMulti = 2) }
}

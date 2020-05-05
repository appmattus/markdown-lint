package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory

class UlIndentRuleTest {

    @TestFactory
    fun ulIndentRule() = FileTestFactory(
        exclude = listOf(
            "bulleted_list_2_space_indent.md",
            "spaces_after_list_marker.md",
            "bulleted_list_not_at_beginning_of_line.md"
        )
    ) { UlIndentRule() }

    @TestFactory
    fun `ulIndentRule indent 4`() = FileTestFactory(
        listOf(
            "spaces_after_list_marker.md",
            "bulleted_list_2_space_indent.md",
            "ul_indent_bugfixes_4_spaces.md"
        )
    ) { UlIndentRule(indent = 4) }

    @TestFactory
    fun `ulIndentRule bugfixes`() = FileTestFactory(listOf("ul_indent_bugfixes.md")) { UlIndentRule() }
}

package com.appmattus.markdown.rules

import kotlin.test.Test

class UlIndentRuleTest {
    @Test
    fun `ul_indent_bugfixes`() = fileRuleScenario(listOf("ul_indent_bugfixes.md")) { UlIndentRule() }

    @Test
    fun `ul_indent_bugfixes_4_spaces`() = fileRuleScenario(
        listOf("ul_indent_bugfixes_4_spaces.md")
    ) { UlIndentRule(indent = 4) }

    @Test
    fun `bulleted_list_2_space_indent`() = fileRuleScenario(
        listOf("bulleted_list_2_space_indent.md")
    ) { UlIndentRule(indent = 4) }

    @Test
    fun `spaces_after_list_marker`() = fileRuleScenario(
        listOf("spaces_after_list_marker.md")
    ) { UlIndentRule(indent = 4) }

    @Test
    fun `all files except bulleted_list_2_space_indent, spaces_after_list_marker, bulleted_list_not_at_beginning_of_line`() = fileRuleScenario(
        exclude = listOf(
            "bulleted_list_2_space_indent.md",
            "spaces_after_list_marker.md",
            "bulleted_list_not_at_beginning_of_line.md"
        )
    ) { UlIndentRule() }
}

package com.appmattus.markdown.rules

import kotlin.test.Test

class SingleH1RuleTest {
    @Test
    fun `alternate_top_level_header`() = fileRuleScenario(
        listOf("alternate_top_level_header.md")
    ) { SingleH1Rule(level = 2) }

    @Test
    fun `all files except alternate_top_level_header`() = fileRuleScenario(
        exclude = listOf("alternate_top_level_header.md")
    ) { SingleH1Rule() }
}

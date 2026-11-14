package com.appmattus.markdown.rules

import kotlin.test.Test

class NoTrailingSpacesRuleTest {
    @Test
    fun `trailing_spaces_br`() = fileRuleScenario(
        listOf("trailing_spaces_br.md")
    ) { NoTrailingSpacesRule(brSpaces = 2) }

    @Test
    fun `all files except trailing_spaces_br`() = fileRuleScenario(
        exclude = listOf("trailing_spaces_br.md")
    ) { NoTrailingSpacesRule() }
}

package com.appmattus.markdown.rules

import kotlin.test.Test

class BlanksAroundListsRuleTest {
    @Test
    fun `all files except break-all-the-rules`() = fileRuleScenario(
        exclude = listOf("break-all-the-rules.md")
    ) { BlanksAroundListsRule() }
}

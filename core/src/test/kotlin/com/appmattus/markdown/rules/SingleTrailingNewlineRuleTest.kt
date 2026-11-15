package com.appmattus.markdown.rules

import kotlin.test.Test

class SingleTrailingNewlineRuleTest {
    @Test
    fun `no_newline`() = fileRuleScenario(listOf("no_newline.md")) { SingleTrailingNewlineRule() }

    @Test
    fun `has_newline`() = fileRuleScenario(listOf("has_newline.md")) { SingleTrailingNewlineRule() }
}

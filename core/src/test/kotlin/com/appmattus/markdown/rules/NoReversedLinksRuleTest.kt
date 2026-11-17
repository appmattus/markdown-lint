package com.appmattus.markdown.rules

import kotlin.test.Test

class NoReversedLinksRuleTest {
    @Test
    fun `all files except empty-links`() = fileRuleScenario(
        exclude = listOf("empty-links.md")
    ) { NoReversedLinksRule() }
}

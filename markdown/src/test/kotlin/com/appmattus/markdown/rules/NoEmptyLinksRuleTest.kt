package com.appmattus.markdown.rules

import kotlin.test.Test

class NoEmptyLinksRuleTest {
    @Test
    fun `all files except reversed_link`() = fileRuleScenario(
        exclude = listOf("reversed_link.md")
    ) { NoEmptyLinksRule() }
}

package com.appmattus.markdown.rules

import kotlin.test.Test

class NoInlineHtmlRuleTest {
    @Test
    fun `all files`() = fileRuleScenario { NoInlineHtmlRule() }
}

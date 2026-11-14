package com.appmattus.markdown.rules

import kotlin.test.Test

class NoBareUrlsRuleTest {
    @Test
    fun `all files`() = fileRuleScenario { NoBareUrlsRule() }
}

package com.appmattus.markdown.rules

import kotlin.test.Test

class BlanksAroundHeadersRuleTest {
    @Test
    fun `all files`() = fileRuleScenario { BlanksAroundHeadersRule() }
}

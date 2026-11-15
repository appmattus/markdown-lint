package com.appmattus.markdown.rules

import kotlin.test.Test

class NoMultipleSpaceClosedAtxRuleTest {
    @Test
    fun `all files`() = fileRuleScenario { NoMultipleSpaceClosedAtxRule() }
}

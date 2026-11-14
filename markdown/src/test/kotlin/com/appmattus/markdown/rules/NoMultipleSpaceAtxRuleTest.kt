package com.appmattus.markdown.rules

import kotlin.test.Test

class NoMultipleSpaceAtxRuleTest {
    @Test
    fun `all files`() = fileRuleScenario { NoMultipleSpaceAtxRule() }
}

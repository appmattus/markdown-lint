package com.appmattus.markdown.rules

import kotlin.test.Test

class NoMultipleSpaceBlockquoteRuleTest {
    @Test
    fun `all files`() = fileRuleScenario { NoMultipleSpaceBlockquoteRule() }
}

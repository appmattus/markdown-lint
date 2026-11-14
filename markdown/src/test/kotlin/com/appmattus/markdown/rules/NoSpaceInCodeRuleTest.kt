package com.appmattus.markdown.rules

import kotlin.test.Test

class NoSpaceInCodeRuleTest {
    @Test
    fun `all files`() = fileRuleScenario { NoSpaceInCodeRule() }
}

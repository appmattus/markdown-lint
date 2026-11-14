package com.appmattus.markdown.rules

import kotlin.test.Test

class NoMissingSpaceAtxRuleTest {
    @Test
    fun `all files`() = fileRuleScenario { NoMissingSpaceAtxRule() }
}

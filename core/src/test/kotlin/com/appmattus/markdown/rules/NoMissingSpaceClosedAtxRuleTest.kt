package com.appmattus.markdown.rules

import kotlin.test.Test

class NoMissingSpaceClosedAtxRuleTest {
    @Test
    fun `all files`() = fileRuleScenario { NoMissingSpaceClosedAtxRule() }
}

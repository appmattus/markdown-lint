package com.appmattus.markdown.rules

import kotlin.test.Test

class HeaderIncrementRuleTest {
    @Test
    fun `all files`() = fileRuleScenario { HeaderIncrementRule() }
}

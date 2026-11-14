package com.appmattus.markdown.rules

import kotlin.test.Test

class HeaderStartLeftRuleTest {
    @Test
    fun `all files`() = fileRuleScenario { HeaderStartLeftRule() }
}

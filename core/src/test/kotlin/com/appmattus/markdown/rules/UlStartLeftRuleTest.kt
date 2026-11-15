package com.appmattus.markdown.rules

import kotlin.test.Test

class UlStartLeftRuleTest {
    @Test
    fun `all files`() = fileRuleScenario { UlStartLeftRule() }
}

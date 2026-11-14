package com.appmattus.markdown.rules

import kotlin.test.Test

class NoMultipleBlanksRuleTest {
    @Test
    fun `all files`() = fileRuleScenario { NoMultipleBlanksRule() }
}

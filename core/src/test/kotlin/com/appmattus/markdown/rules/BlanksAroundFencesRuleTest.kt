package com.appmattus.markdown.rules

import kotlin.test.Test

class BlanksAroundFencesRuleTest {
    @Test
    fun `all files`() = fileRuleScenario { BlanksAroundFencesRule() }
}

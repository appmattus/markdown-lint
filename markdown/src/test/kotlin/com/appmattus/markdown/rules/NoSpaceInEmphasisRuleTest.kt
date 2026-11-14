package com.appmattus.markdown.rules

import kotlin.test.Test

class NoSpaceInEmphasisRuleTest {
    @Test
    fun `all files`() = fileRuleScenario { NoSpaceInEmphasisRule() }
}

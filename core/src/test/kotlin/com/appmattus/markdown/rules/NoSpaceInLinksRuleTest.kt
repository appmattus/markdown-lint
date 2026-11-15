package com.appmattus.markdown.rules

import kotlin.test.Test

class NoSpaceInLinksRuleTest {
    @Test
    fun `all files`() = fileRuleScenario { NoSpaceInLinksRule() }
}

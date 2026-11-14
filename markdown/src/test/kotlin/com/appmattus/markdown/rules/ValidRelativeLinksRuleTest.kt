package com.appmattus.markdown.rules

import kotlin.test.Test

class ValidRelativeLinksRuleTest {
    @Test
    fun `all files`() = fileRuleScenario { ValidRelativeLinksRule() }
}

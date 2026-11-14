package com.appmattus.markdown.rules

import kotlin.test.Test

class NoBlanksBlockquoteRuleTest {
    @Test
    fun `all files`() = fileRuleScenario { NoBlanksBlockquoteRule() }
}

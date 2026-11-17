package com.appmattus.markdown.rules

import kotlin.test.Test

class NoEmphasisAsHeaderRuleTest {
    @Test
    fun `all files`() = fileRuleScenario { NoEmphasisAsHeaderRule() }
}

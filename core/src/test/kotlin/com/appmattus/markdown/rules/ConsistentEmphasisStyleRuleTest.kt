package com.appmattus.markdown.rules

import com.appmattus.markdown.rules.config.EmphasisStyle
import kotlin.test.Test

class ConsistentEmphasisStyleRuleTest {
    @Test
    fun `consistent_emphasis_asterisk`() = fileRuleScenario(files = listOf("consistent_emphasis_asterisk.md")) {
        ConsistentEmphasisStyleRule(EmphasisStyle.Asterisk)
    }

    @Test
    fun `consistent_emphasis_underscore`() = fileRuleScenario(files = listOf("consistent_emphasis_underscore.md")) {
        ConsistentEmphasisStyleRule(EmphasisStyle.Underscore)
    }

    @Test
    fun `all files`() = fileRuleScenario { ConsistentEmphasisStyleRule(EmphasisStyle.Consistent) }
}

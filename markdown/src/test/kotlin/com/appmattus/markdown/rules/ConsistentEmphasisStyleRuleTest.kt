package com.appmattus.markdown.rules

import com.appmattus.markdown.rules.config.EmphasisStyle
import org.junit.jupiter.api.TestFactory

class ConsistentEmphasisStyleRuleTest {

    @TestFactory
    fun `consistentEmphasisStyleRule asterisk`() = FileTestFactory(files = listOf("consistent_emphasis_asterisk.md")) {
        ConsistentEmphasisStyleRule(EmphasisStyle.Asterisk)
    }

    @TestFactory
    fun `consistentEmphasisStyleRule underscore`() =
        FileTestFactory(files = listOf("consistent_emphasis_underscore.md")) {
            ConsistentEmphasisStyleRule(EmphasisStyle.Underscore)
        }

    @TestFactory
    fun `consistentEmphasisStyleRule consistent`() =
        FileTestFactory { ConsistentEmphasisStyleRule(EmphasisStyle.Consistent) }
}

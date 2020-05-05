package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory

class NoTrailingPunctuationRuleTest {

    @TestFactory
    fun noTrailingPunctuationRule() =
        FileTestFactory(exclude = listOf("header_trailing_punctuation_customized.md")) { NoTrailingPunctuationRule() }

    @TestFactory
    fun `noTrailingPunctuationRule custom punctuation`() =
        FileTestFactory(listOf("header_trailing_punctuation_customized.md")) { NoTrailingPunctuationRule(punctuation = ".,;:!") }
}

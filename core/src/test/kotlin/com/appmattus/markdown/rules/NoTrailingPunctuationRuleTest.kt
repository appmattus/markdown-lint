package com.appmattus.markdown.rules

import kotlin.test.Test

class NoTrailingPunctuationRuleTest {
    @Test
    fun `header_trailing_punctuation_customized`() = fileRuleScenario(
        listOf("header_trailing_punctuation_customized.md")
    ) {
        NoTrailingPunctuationRule(punctuation = ".,;:!")
    }

    @Test
    fun `all files except header_trailing_punctuation_customized`() = fileRuleScenario(
        exclude = listOf("header_trailing_punctuation_customized.md")
    ) {
        NoTrailingPunctuationRule()
    }
}

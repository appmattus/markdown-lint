package com.appmattus.markdown.rules

import kotlin.test.Test

class NoDuplicateHeaderRuleTest {
    @Test
    fun `header_duplicate_content_different_nesting`() = fileRuleScenario(
        listOf("header_duplicate_content_different_nesting.md")
    ) {
        NoDuplicateHeaderRule(
            allowDifferentNesting = true
        )
    }

    @Test
    fun `header_duplicate_content_no_different_nesting_2`() = fileRuleScenario(
        listOf("header_duplicate_content_no_different_nesting_2.md")
    ) {
        NoDuplicateHeaderRule(
            allowDifferentNesting = true
        )
    }

    @Test
    fun `all files except header_duplicate_content_different_nesting`() = fileRuleScenario(
        exclude = listOf("header_duplicate_content_different_nesting.md")
    ) { NoDuplicateHeaderRule() }
}

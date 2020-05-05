package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory

class NoDuplicateHeaderRuleTest {

    @TestFactory
    fun `noDuplicateHeaderRule allow different nesting`() =
        FileTestFactory(
            listOf(
                "header_duplicate_content_different_nesting.md",
                "header_duplicate_content_no_different_nesting_2.md"
            )
        ) {
            NoDuplicateHeaderRule(allowDifferentNesting = true)
        }

    @TestFactory
    fun noDuplicateHeaderRule() =
        FileTestFactory(exclude = listOf("header_duplicate_content_different_nesting.md")) { NoDuplicateHeaderRule() }
}

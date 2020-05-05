package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory

class ListIndentRuleTest {

    @TestFactory
    fun listIndentRule() = FileTestFactory { ListIndentRule() }

    @TestFactory
    fun `listIndentRule different parents`() =
        FileTestFactory(files = listOf("list-indent-rule-different-parents.md")) { ListIndentRule() }
}

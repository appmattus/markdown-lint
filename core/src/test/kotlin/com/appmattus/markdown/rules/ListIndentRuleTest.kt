package com.appmattus.markdown.rules

import kotlin.test.Test

class ListIndentRuleTest {
    @Test
    fun `list-indent-rule-different-parents`() = fileRuleScenario(
        files = listOf("list-indent-rule-different-parents.md")
    ) { ListIndentRule() }

    @Test
    fun `all files`() = fileRuleScenario { ListIndentRule() }
}

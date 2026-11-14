package com.appmattus.markdown.rules

import com.appmattus.markdown.rules.config.CodeBlockStyle
import kotlin.test.Test

class CodeBlockStyleRuleTest {
    @Test
    fun `code_block_consistency`() = fileRuleScenario(
        listOf("code_block_consistency.md")
    ) { CodeBlockStyleRule(style = CodeBlockStyle.Consistent) }

    @Test
    fun `code_block_fenced`() = fileRuleScenario(
        listOf("code_block_fenced.md")
    ) { CodeBlockStyleRule(style = CodeBlockStyle.Fenced) }

    @Test
    fun `code_block_indented`() = fileRuleScenario(
        listOf("code_block_indented.md")
    ) { CodeBlockStyleRule(style = CodeBlockStyle.Indented) }
}

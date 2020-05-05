package com.appmattus.markdown.rules

import com.appmattus.markdown.rules.config.CodeBlockStyle
import org.junit.jupiter.api.TestFactory

class CodeBlockStyleRuleTest {

    @TestFactory
    fun `codeBlockStyleRule consistent`() =
        FileTestFactory(listOf("code_block_consistency.md")) { CodeBlockStyleRule(style = CodeBlockStyle.Consistent) }

    @TestFactory
    fun `codeBlockStyleRule fenced`() =
        FileTestFactory(listOf("code_block_fenced.md")) { CodeBlockStyleRule(style = CodeBlockStyle.Fenced) }

    @TestFactory
    fun `codeBlockStyleRule indented`() =
        FileTestFactory(listOf("code_block_indented.md")) { CodeBlockStyleRule(style = CodeBlockStyle.Indented) }
}

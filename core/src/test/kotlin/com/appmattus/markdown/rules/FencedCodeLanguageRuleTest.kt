package com.appmattus.markdown.rules

import kotlin.test.Test

class FencedCodeLanguageRuleTest {
    @Test
    fun `all files except code_block_consistency, code_block_fenced, code_block_indented, fenced_code_without_blank_lines`() = fileRuleScenario(
        exclude = listOf(
            "code_block_consistency.md",
            "code_block_fenced.md",
            "code_block_indented.md",
            "fenced_code_without_blank_lines.md"
        )
    ) { FencedCodeLanguageRule() }
}

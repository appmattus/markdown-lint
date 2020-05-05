package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory

class FencedCodeLanguageRuleTest {

    @TestFactory
    fun fencedCodeLanguageRule() = FileTestFactory(
        exclude = listOf(
            "code_block_consistency.md",
            "code_block_fenced.md",
            "code_block_indented.md",
            "fenced_code_without_blank_lines.md"
        )
    ) { FencedCodeLanguageRule() }
}

package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory

class SingleH1RuleTest {

    @TestFactory
    fun singleH1Rule() = FileTestFactory(exclude = listOf("alternate_top_level_header.md")) { SingleH1Rule() }

    @TestFactory
    fun `singleH1Rule level 2`() = FileTestFactory(listOf("alternate_top_level_header.md")) { SingleH1Rule(level = 2) }
}

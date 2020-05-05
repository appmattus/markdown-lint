package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory

class FirstHeaderH1RuleTest {

    @TestFactory
    fun `firstHeaderH1Rule level 2`() =
        FileTestFactory(listOf("alternate_top_level_header.md")) { FirstHeaderH1Rule(level = 2) }

    @TestFactory
    fun `firstHeaderH1Rule default level`() =
        FileTestFactory(exclude = listOf("alternate_top_level_header.md")) { FirstHeaderH1Rule() }
}

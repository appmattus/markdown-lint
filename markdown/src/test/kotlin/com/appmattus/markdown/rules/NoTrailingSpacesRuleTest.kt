package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory

class NoTrailingSpacesRuleTest {

    @TestFactory
    fun noTrailingSpacesRule() = FileTestFactory(exclude = listOf("trailing_spaces_br.md")) { NoTrailingSpacesRule() }

    @TestFactory
    fun `noTrailingSpacesRule br spaces 2`() =
        FileTestFactory(listOf("trailing_spaces_br.md")) { NoTrailingSpacesRule(brSpaces = 2) }
}

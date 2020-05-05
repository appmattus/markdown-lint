package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory

class NoEmptyLinksRuleTest {

    @TestFactory
    fun noEmptyLinksRule() = FileTestFactory(exclude = listOf("reversed_link.md")) { NoEmptyLinksRule() }
}

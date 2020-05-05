package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory

class NoReversedLinksRuleTest {

    @TestFactory
    fun noReversedLinksRule() = FileTestFactory(exclude = listOf("empty-links.md")) { NoReversedLinksRule() }
}

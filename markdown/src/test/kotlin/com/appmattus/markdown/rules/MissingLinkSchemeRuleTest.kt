package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory

class MissingLinkSchemeRuleTest {

    @TestFactory
    fun `missingLinkSchemeRule relative links`() =
        FileTestFactory(listOf("relative-links.md")) { MissingLinkSchemeRule() }

    @TestFactory
    fun missingLinkSchemeRule() = FileTestFactory { MissingLinkSchemeRule() }
}

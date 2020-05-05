package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory

class NoSpaceInLinksRuleTest {

    @TestFactory
    fun noSpaceInLinksRule() = FileTestFactory { NoSpaceInLinksRule() }
}

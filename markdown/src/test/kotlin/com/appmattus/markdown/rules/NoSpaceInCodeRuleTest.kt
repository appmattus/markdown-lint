package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory

class NoSpaceInCodeRuleTest {

    @TestFactory
    fun noSpaceInCodeRule() = FileTestFactory { NoSpaceInCodeRule() }
}

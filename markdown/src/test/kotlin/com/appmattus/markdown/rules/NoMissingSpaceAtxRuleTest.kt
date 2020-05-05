package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory

class NoMissingSpaceAtxRuleTest {

    @TestFactory
    fun noMissingSpaceAtxRule() = FileTestFactory { NoMissingSpaceAtxRule() }
}

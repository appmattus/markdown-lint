package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory

class NoMissingSpaceClosedAtxRuleTest {

    @TestFactory
    fun noMissingSpaceClosedAtxRule() = FileTestFactory { NoMissingSpaceClosedAtxRule() }
}

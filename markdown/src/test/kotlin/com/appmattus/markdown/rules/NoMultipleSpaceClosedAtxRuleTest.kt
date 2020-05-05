package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory

class NoMultipleSpaceClosedAtxRuleTest {

    @TestFactory
    fun noMultipleSpaceClosedAtxRule() = FileTestFactory { NoMultipleSpaceClosedAtxRule() }
}

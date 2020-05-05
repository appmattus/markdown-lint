package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory

class NoMultipleSpaceAtxRuleTest {

    @TestFactory
    fun noMultipleSpaceAtxRule() = FileTestFactory { NoMultipleSpaceAtxRule() }
}

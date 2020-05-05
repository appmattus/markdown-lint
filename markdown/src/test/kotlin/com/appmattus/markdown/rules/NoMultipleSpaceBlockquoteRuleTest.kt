package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory

class NoMultipleSpaceBlockquoteRuleTest {

    @TestFactory
    fun noMultipleSpaceBlockquoteRule() = FileTestFactory { NoMultipleSpaceBlockquoteRule() }
}

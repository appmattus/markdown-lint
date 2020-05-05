package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory

class HeaderIncrementRuleTest {

    @TestFactory
    fun headerIncrementRule() = FileTestFactory { HeaderIncrementRule() }
}

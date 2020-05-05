package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory

class HeaderStartLeftRuleTest {

    @TestFactory
    fun headerStartLeftRule() = FileTestFactory { HeaderStartLeftRule() }
}

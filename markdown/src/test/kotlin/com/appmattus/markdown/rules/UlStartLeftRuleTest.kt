package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory

class UlStartLeftRuleTest {

    @TestFactory
    fun ulStartLeftRule() = FileTestFactory { UlStartLeftRule() }
}

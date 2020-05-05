package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory

class BlanksAroundHeadersRuleTest {

    @TestFactory
    fun blanksAroundHeadersRule() = FileTestFactory { BlanksAroundHeadersRule() }
}

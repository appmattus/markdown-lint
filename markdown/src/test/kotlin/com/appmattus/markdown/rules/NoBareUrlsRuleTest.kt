package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory

class NoBareUrlsRuleTest {

    @TestFactory
    fun noBareUrlsRule() = FileTestFactory { NoBareUrlsRule() }
}

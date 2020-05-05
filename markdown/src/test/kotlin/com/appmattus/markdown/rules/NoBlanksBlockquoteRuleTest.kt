package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory

class NoBlanksBlockquoteRuleTest {

    @TestFactory
    fun noBlanksBlockquoteRule() = FileTestFactory { NoBlanksBlockquoteRule() }
}

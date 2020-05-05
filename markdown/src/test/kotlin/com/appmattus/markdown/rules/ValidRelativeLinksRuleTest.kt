package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory

class ValidRelativeLinksRuleTest {

    @TestFactory
    fun validRelativeLinksRule() = FileTestFactory { ValidRelativeLinksRule() }
}

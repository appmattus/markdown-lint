package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory

class NoMultipleBlanksRuleTest {

    @TestFactory
    fun noMultipleBlanksRule() = FileTestFactory { NoMultipleBlanksRule() }
}

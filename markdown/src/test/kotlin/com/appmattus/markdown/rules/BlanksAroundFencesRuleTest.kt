package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory

class BlanksAroundFencesRuleTest {

    @TestFactory
    fun blanksAroundFencesRule() = FileTestFactory { BlanksAroundFencesRule() }
}

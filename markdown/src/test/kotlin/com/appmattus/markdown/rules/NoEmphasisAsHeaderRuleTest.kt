package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory

class NoEmphasisAsHeaderRuleTest {

    @TestFactory
    fun noEmphasisAsHeaderRule() = FileTestFactory { NoEmphasisAsHeaderRule() }
}

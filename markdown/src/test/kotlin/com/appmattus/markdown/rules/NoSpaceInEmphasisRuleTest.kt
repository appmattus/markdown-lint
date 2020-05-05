package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory

class NoSpaceInEmphasisRuleTest {

    @TestFactory
    fun noSpaceInEmphasisRule() = FileTestFactory { NoSpaceInEmphasisRule() }
}

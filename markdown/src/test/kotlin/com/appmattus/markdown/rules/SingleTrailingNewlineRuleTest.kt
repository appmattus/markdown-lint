package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory

class SingleTrailingNewlineRuleTest {

    @TestFactory
    fun singleTrailingNewlineRule() =
        FileTestFactory(listOf("no_newline.md", "has_newline.md")) { SingleTrailingNewlineRule() }
}

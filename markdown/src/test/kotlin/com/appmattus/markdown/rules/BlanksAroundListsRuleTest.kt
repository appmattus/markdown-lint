package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory

class BlanksAroundListsRuleTest {

    @TestFactory
    fun blanksAroundListsRule() =
        FileTestFactory(exclude = listOf("break-all-the-rules.md")) { BlanksAroundListsRule() }
}

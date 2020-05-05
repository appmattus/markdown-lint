package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory

class CommandsShowOutputRuleTest {

    @TestFactory
    fun commandsShowOutputRule() = FileTestFactory { CommandsShowOutputRule() }
}

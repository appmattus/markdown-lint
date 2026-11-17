package com.appmattus.markdown.rules

import kotlin.test.Test

class CommandsShowOutputRuleTest {
    @Test
    fun `all files`() = fileRuleScenario { CommandsShowOutputRule() }
}

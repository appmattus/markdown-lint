package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory

class NoHardTabsRuleTest {

    @TestFactory
    fun noHardTabsRule() = FileTestFactory { NoHardTabsRule() }

    @TestFactory
    fun `noHardTabsRule issues`() = FileTestFactory(listOf("nohardtabs_issues.md")) { NoHardTabsRule() }
}

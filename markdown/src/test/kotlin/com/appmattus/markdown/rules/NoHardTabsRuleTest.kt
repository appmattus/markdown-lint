package com.appmattus.markdown.rules

import kotlin.test.Test

class NoHardTabsRuleTest {
    @Test
    fun `nohardtabs_issues`() = fileRuleScenario(listOf("nohardtabs_issues.md")) { NoHardTabsRule() }

    @Test
    fun `all files`() = fileRuleScenario { NoHardTabsRule() }
}

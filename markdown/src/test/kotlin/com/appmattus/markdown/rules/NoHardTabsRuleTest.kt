package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object NoHardTabsRuleTest : Spek({
    Feature("NoHardTabsRule") {
        FileRuleScenario(listOf("nohardtabs_issues.md")) { NoHardTabsRule() }

        FileRuleScenario { NoHardTabsRule() }
    }
})

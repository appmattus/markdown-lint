package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object MD035Test : Spek({
    Feature("MD035") {
        FileRuleScenario(listOf("hr_style_dashes.md")) { MD035(style = HorizontalRuleStyle.Dash) }

        FileRuleScenario(listOf("hr_style_long.md")) { MD035(style = HorizontalRuleStyle.Exact("_____")) }

        FileRuleScenario(listOf("hr_style_stars.md")) { MD035(style = HorizontalRuleStyle.Asterisk) }

        FileRuleScenario(exclude = listOf("hr_style_dashes.md", "hr_style_long.md", "hr_style_stars.md")) { MD035() }
    }
})

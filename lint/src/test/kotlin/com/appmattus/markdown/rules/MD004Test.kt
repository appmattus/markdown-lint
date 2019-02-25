package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object MD004Test : Spek({
    Feature("MD004") {
        FileRuleScenario(listOf("incorrect_bullet_style_asterisk.md")) { MD004(UnorderedListStyle.Asterisk) }

        FileRuleScenario(listOf("incorrect_bullet_style_dash.md")) { MD004(UnorderedListStyle.Dash) }

        FileRuleScenario(listOf("incorrect_bullet_style_plus.md")) { MD004(UnorderedListStyle.Plus) }

        FileRuleScenario(
            exclude = listOf(
                "incorrect_bullet_style_asterisk.md",
                "incorrect_bullet_style_dash.md",
                "incorrect_bullet_style_plus.md",
                "lists_without_blank_lines.md"
            )
        ) { MD004() }
    }
})

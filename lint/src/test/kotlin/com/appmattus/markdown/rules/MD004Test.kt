package com.appmattus.markdown.rules

import com.appmattus.markdown.rules.config.UnorderedListStyle
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object MD004Test : Spek({
    Feature("ConsistentUlStyleRule") {
        FileRuleScenario(listOf("incorrect_bullet_style_asterisk.md")) { ConsistentUlStyleRule(UnorderedListStyle.Asterisk) }

        FileRuleScenario(listOf("incorrect_bullet_style_dash.md")) { ConsistentUlStyleRule(UnorderedListStyle.Dash) }

        FileRuleScenario(listOf("incorrect_bullet_style_plus.md")) { ConsistentUlStyleRule(UnorderedListStyle.Plus) }

        FileRuleScenario(
            exclude = listOf(
                "incorrect_bullet_style_asterisk.md",
                "incorrect_bullet_style_dash.md",
                "incorrect_bullet_style_plus.md",
                "lists_without_blank_lines.md"
            )
        ) { ConsistentUlStyleRule() }
    }
})

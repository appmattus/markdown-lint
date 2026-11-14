package com.appmattus.markdown.rules

import com.appmattus.markdown.rules.config.UnorderedListStyle
import kotlin.test.Test

class ConsistentUlStyleRuleTest {
    @Test
    fun `incorrect_bullet_style_asterisk`() = fileRuleScenario(
        listOf("incorrect_bullet_style_asterisk.md")
    ) { ConsistentUlStyleRule(UnorderedListStyle.Asterisk) }

    @Test
    fun `incorrect_bullet_style_dash`() = fileRuleScenario(listOf("incorrect_bullet_style_dash.md")) {
        ConsistentUlStyleRule(UnorderedListStyle.Dash)
    }

    @Test
    fun `incorrect_bullet_style_plus`() = fileRuleScenario(listOf("incorrect_bullet_style_plus.md")) {
        ConsistentUlStyleRule(UnorderedListStyle.Plus)
    }

    @Test
    fun `all files except incorrect_bullet_style_asterisk, incorrect_bullet_style_dash, incorrect_bullet_style_plus, lists_without_blank_lines`() = fileRuleScenario(
        exclude = listOf(
            "incorrect_bullet_style_asterisk.md",
            "incorrect_bullet_style_dash.md",
            "incorrect_bullet_style_plus.md",
            "lists_without_blank_lines.md"
        )
    ) { ConsistentUlStyleRule(UnorderedListStyle.Consistent) }
}

package com.appmattus.markdown.rules

import com.appmattus.markdown.rules.config.UnorderedListStyle
import org.junit.jupiter.api.TestFactory

class ConsistentUlStyleRuleTest {

    @TestFactory
    fun `consistentUlStyleRule consistent`() = FileTestFactory(
        exclude = listOf(
            "incorrect_bullet_style_asterisk.md",
            "incorrect_bullet_style_dash.md",
            "incorrect_bullet_style_plus.md",
            "lists_without_blank_lines.md"
        )
    ) { ConsistentUlStyleRule(UnorderedListStyle.Consistent) }

    @TestFactory
    fun `consistentUlStyleRule plus`() =
        FileTestFactory(listOf("incorrect_bullet_style_plus.md")) { ConsistentUlStyleRule(UnorderedListStyle.Plus) }

    @TestFactory
    fun `consistentUlStyleRule dash`() =
        FileTestFactory(listOf("incorrect_bullet_style_dash.md")) { ConsistentUlStyleRule(UnorderedListStyle.Dash) }

    @TestFactory
    fun `consistentUlStyleRule asterisk`() =
        FileTestFactory(listOf("incorrect_bullet_style_asterisk.md")) { ConsistentUlStyleRule(UnorderedListStyle.Asterisk) }
}

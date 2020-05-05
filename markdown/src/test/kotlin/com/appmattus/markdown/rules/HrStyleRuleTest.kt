package com.appmattus.markdown.rules

import com.appmattus.markdown.rules.config.HorizontalRuleStyle
import org.junit.jupiter.api.TestFactory

class HrStyleRuleTest {

    @TestFactory
    fun hrStyleRule() = FileTestFactory(
        exclude = listOf("hr_style_dashes.md", "hr_style_long.md", "hr_style_stars.md", "hr_style_underscores.md")
    ) {
        HrStyleRule(HorizontalRuleStyle.Consistent)
    }

    @TestFactory
    fun `hrStyleRule underscore`() =
        FileTestFactory(listOf("hr_style_underscores.md")) { HrStyleRule(style = HorizontalRuleStyle.Underscore) }

    @TestFactory
    fun `hrStyleRule asterisk`() =
        FileTestFactory(listOf("hr_style_stars.md")) { HrStyleRule(style = HorizontalRuleStyle.Asterisk) }

    @TestFactory
    fun `hrStyleRule exact`() =
        FileTestFactory(listOf("hr_style_long.md")) { HrStyleRule(style = HorizontalRuleStyle.Exact("_____")) }

    @TestFactory
    fun `hrStyleRule dash`() =
        FileTestFactory(listOf("hr_style_dashes.md")) { HrStyleRule(style = HorizontalRuleStyle.Dash) }
}

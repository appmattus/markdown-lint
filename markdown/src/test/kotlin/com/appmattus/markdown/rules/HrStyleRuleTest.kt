package com.appmattus.markdown.rules

import com.appmattus.markdown.rules.config.HorizontalRuleStyle
import kotlin.test.Test

class HrStyleRuleTest {
    @Test
    fun `hr_style_dashes`() = fileRuleScenario(listOf("hr_style_dashes.md")) {
        HrStyleRule(style = HorizontalRuleStyle.Dash)
    }

    @Test
    fun `hr_style_long`() = fileRuleScenario(listOf("hr_style_long.md")) {
        HrStyleRule(style = HorizontalRuleStyle.Exact("_____"))
    }

    @Test
    fun `hr_style_stars`() = fileRuleScenario(listOf("hr_style_stars.md")) {
        HrStyleRule(style = HorizontalRuleStyle.Asterisk)
    }

    @Test
    fun `hr_style_underscores`() = fileRuleScenario(listOf("hr_style_underscores.md")) {
        HrStyleRule(style = HorizontalRuleStyle.Underscore)
    }

    @Test
    fun `all files except hr_style_dashes, hr_style_long, hr_style_stars, hr_style_underscores`() = fileRuleScenario(
        exclude = listOf(
            "hr_style_dashes.md",
            "hr_style_long.md",
            "hr_style_stars.md",
            "hr_style_underscores.md"
        )
    ) {
        HrStyleRule(HorizontalRuleStyle.Consistent)
    }
}

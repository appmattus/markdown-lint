package com.appmattus.markdown.rules

import com.appmattus.markdown.rules.config.HeaderStyle
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object ConsistentHeaderStyleRuleTest : Spek({
    Feature("ConsistentHeaderStyleRule") {
        FileRuleScenario(listOf("headers_good_setext_with_atx.md")) { ConsistentHeaderStyleRule(HeaderStyle.SetextWithAtx) }

        FileRuleScenario(listOf("incorrect_header_atx_closed.md")) { ConsistentHeaderStyleRule(HeaderStyle.AtxClosed) }

        FileRuleScenario(listOf("incorrect_header_atx.md")) { ConsistentHeaderStyleRule(HeaderStyle.Atx) }

        FileRuleScenario(listOf("incorrect_header_setext.md")) { ConsistentHeaderStyleRule(HeaderStyle.Setext) }

        FileRuleScenario(listOf("incorrect_header_setext_with_atx_1.md")) {
            ConsistentHeaderStyleRule(HeaderStyle.SetextWithAtx)
        }

        FileRuleScenario(listOf("incorrect_header_setext_with_atx_2.md")) {
            ConsistentHeaderStyleRule(HeaderStyle.SetextWithAtx)
        }

        FileRuleScenario(
            exclude = listOf(
                "headers_good_setext_with_atx.md",
                "incorrect_header_atx_closed.md",
                "incorrect_header_atx.md",
                "incorrect_header_setext.md",
                "incorrect_header_setext_with_atx_1.md",
                "incorrect_header_setext_with_atx_2.md",
                "headers_with_spaces_at_the_beginning.md"
            )
        ) { ConsistentHeaderStyleRule() }
    }
})

package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object MD003Test : Spek({
    Feature("MD003") {
        FileRuleScenario(listOf("headers_good_setext_with_atx.md")) { MD003(HeaderStyle.SetextWithAtx) }

        FileRuleScenario(listOf("incorrect_header_atx_closed.md")) { MD003(HeaderStyle.AtxClosed) }

        FileRuleScenario(listOf("incorrect_header_atx.md")) { MD003(HeaderStyle.Atx) }

        FileRuleScenario(listOf("incorrect_header_setext.md")) { MD003(HeaderStyle.Setext) }

        FileRuleScenario(
            exclude = listOf(
                "headers_good_setext_with_atx.md",
                "incorrect_header_atx_closed.md",
                "incorrect_header_atx.md",
                "incorrect_header_setext.md",
                "headers_with_spaces_at_the_beginning.md"
            )
        ) { MD003() }
    }
})

package com.appmattus.markdown.rules

import com.appmattus.markdown.rules.config.FilenameStyle
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object FN003Test : Spek({
    val consistentRule1 = FN003()
    val consistentRule2 = FN003()
    val consistentRule3 = FN003()
    val dashRule = FN003(style = FilenameStyle.Dash)
    val underscoreRule = FN003(style = FilenameStyle.Underscore)

    Feature("FN003") {
        FileRuleScenario(listOf("alternate_top_level_header.md", "atx_closed_header_spacing.md")) { consistentRule1 }

        FileRuleScenario(listOf("break-all-the-rules.md", "empty-links.md")) { consistentRule2 }

        FileRuleScenario(listOf("break-all-the-rules.md", "empty-links.md")) { dashRule }

        FileRuleScenario(listOf("alternate_top_level_header.md", "atx_closed_header_spacing.md")) { underscoreRule }

        FileRuleScenario(listOf("alternate_top_level_header.md", "empty-links.md")) { consistentRule3 }


    }
})

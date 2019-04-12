package com.appmattus.markdown.rules

import com.appmattus.markdown.rules.config.EmphasisStyle
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object ConsistentEmphasisStyleRuleTest : Spek({
    Feature("ConsistentEmphasisStyleRule") {
        FileRuleScenario(files = listOf("consistent_emphasis_asterisk.md")) {
            ConsistentEmphasisStyleRule(EmphasisStyle.Asterisk)
        }

        FileRuleScenario(files = listOf("consistent_emphasis_underscore.md")) {
            ConsistentEmphasisStyleRule(EmphasisStyle.Underscore)
        }

        FileRuleScenario { ConsistentEmphasisStyleRule(EmphasisStyle.Consistent) }
    }
})

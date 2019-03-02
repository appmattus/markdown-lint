package com.appmattus.markdown.rules

import com.appmattus.markdown.rules.config.OrderedListStyle
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object OlPrefixRuleTest : Spek({
    Feature("OlPrefixRule") {
        FileRuleScenario(listOf("ordered_list_item_prefix_ordered.md")) { OlPrefixRule(style = OrderedListStyle.Ordered) }

        FileRuleScenario(exclude = listOf("ordered_list_item_prefix_ordered.md")) { OlPrefixRule() }
    }
})

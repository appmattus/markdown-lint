package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object MD029Test : Spek({
    Feature("MD029") {
        FileRuleScenario(listOf("ordered_list_item_prefix_ordered.md")) { MD029(style = OrderedListStyle.Ordered) }

        FileRuleScenario(exclude = listOf("ordered_list_item_prefix_ordered.md")) { MD029() }
    }
})

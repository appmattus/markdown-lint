package com.appmattus.markdown.rules

import com.appmattus.markdown.rules.config.OrderedListStyle
import kotlin.test.Test

class OlPrefixRuleTest {
    @Test
    fun `ordered_list_item_prefix_ordered`() = fileRuleScenario(
        listOf("ordered_list_item_prefix_ordered.md")
    ) { OlPrefixRule(style = OrderedListStyle.Ordered) }

    @Test
    fun `all files except ordered_list_item_prefix_ordered`() = fileRuleScenario(
        exclude = listOf("ordered_list_item_prefix_ordered.md")
    ) { OlPrefixRule() }
}

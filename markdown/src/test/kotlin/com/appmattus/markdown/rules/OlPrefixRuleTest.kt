package com.appmattus.markdown.rules

import com.appmattus.markdown.rules.config.OrderedListStyle
import org.junit.jupiter.api.TestFactory

class OlPrefixRuleTest {

    @TestFactory
    fun olPrefixRule() = FileTestFactory(exclude = listOf("ordered_list_item_prefix_ordered.md")) { OlPrefixRule() }

    @TestFactory
    fun `olPrefixRule ordered`() =
        FileTestFactory(listOf("ordered_list_item_prefix_ordered.md")) { OlPrefixRule(style = OrderedListStyle.Ordered) }
}

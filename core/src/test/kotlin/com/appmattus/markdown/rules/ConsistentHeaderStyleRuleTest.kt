package com.appmattus.markdown.rules

import com.appmattus.markdown.rules.config.HeaderStyle
import kotlin.test.Test

class ConsistentHeaderStyleRuleTest {
    @Test
    fun `headers_good_setext_with_atx`() = fileRuleScenario(
        listOf("headers_good_setext_with_atx.md")
    ) { ConsistentHeaderStyleRule(HeaderStyle.SetextWithAtx) }

    @Test
    fun `incorrect_header_atx_closed`() = fileRuleScenario(
        listOf("incorrect_header_atx_closed.md")
    ) { ConsistentHeaderStyleRule(HeaderStyle.AtxClosed) }

    @Test
    fun `incorrect_header_atx`() = fileRuleScenario(
        listOf("incorrect_header_atx.md")
    ) { ConsistentHeaderStyleRule(HeaderStyle.Atx) }

    @Test
    fun `incorrect_header_setext`() = fileRuleScenario(
        listOf("incorrect_header_setext.md")
    ) { ConsistentHeaderStyleRule(HeaderStyle.Setext) }

    @Test
    fun `incorrect_header_setext_with_atx_1`() = fileRuleScenario(listOf("incorrect_header_setext_with_atx_1.md")) {
        ConsistentHeaderStyleRule(HeaderStyle.SetextWithAtx)
    }

    @Test
    fun `incorrect_header_setext_with_atx_2`() = fileRuleScenario(listOf("incorrect_header_setext_with_atx_2.md")) {
        ConsistentHeaderStyleRule(HeaderStyle.SetextWithAtx)
    }

    @Test
    fun `all files except headers_good_setext_with_atx, incorrect_header_atx_closed, incorrect_header_atx, incorrect_header_setext, incorrect_header_setext_with_atx_1, incorrect_header_setext_with_atx_2, headers_with_spaces_at_the_beginning`() = fileRuleScenario(
        exclude = listOf(
            "headers_good_setext_with_atx.md",
            "incorrect_header_atx_closed.md",
            "incorrect_header_atx.md",
            "incorrect_header_setext.md",
            "incorrect_header_setext_with_atx_1.md",
            "incorrect_header_setext_with_atx_2.md",
            "headers_with_spaces_at_the_beginning.md"
        )
    ) { ConsistentHeaderStyleRule(HeaderStyle.Consistent) }
}

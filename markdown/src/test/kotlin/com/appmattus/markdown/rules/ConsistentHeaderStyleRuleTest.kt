package com.appmattus.markdown.rules

import com.appmattus.markdown.rules.config.HeaderStyle
import org.junit.jupiter.api.TestFactory

class ConsistentHeaderStyleRuleTest {

    @TestFactory
    fun `consistentHeaderStyleRule setextWithAtx`() = FileTestFactory(
        listOf(
            "headers_good_setext_with_atx.md",
            "incorrect_header_setext_with_atx_1.md",
            "incorrect_header_setext_with_atx_2.md"
        )
    ) { ConsistentHeaderStyleRule(HeaderStyle.SetextWithAtx) }

    @TestFactory
    fun `consistentHeaderStyleRule atxClosed`() =
        FileTestFactory(listOf("incorrect_header_atx_closed.md")) { ConsistentHeaderStyleRule(HeaderStyle.AtxClosed) }

    @TestFactory
    fun `consistentHeaderStyleRule atxOpen`() =
        FileTestFactory(listOf("incorrect_header_atx.md")) { ConsistentHeaderStyleRule(HeaderStyle.Atx) }

    @TestFactory
    fun `consistentHeaderStyleRule setext`() =
        FileTestFactory(listOf("incorrect_header_setext.md")) { ConsistentHeaderStyleRule(HeaderStyle.Setext) }

    @TestFactory
    fun `consistentHeaderStyleRule consistent`() = FileTestFactory(
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

package com.appmattus.markdown.rules

import kotlin.test.Test

class MissingLinkSchemeRuleTest {
    @Test
    fun `relative-links`() = fileRuleScenario(listOf("relative-links.md")) { MissingLinkSchemeRule() }

    @Test
    fun `all files`() = fileRuleScenario { MissingLinkSchemeRule() }
}

package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object MissingLinkSchemeRuleTest : Spek({
    Feature("MissingLinkSchemeRule") {
        FileRuleScenario(listOf("relative-links.md")) { MissingLinkSchemeRule() }

        FileRuleScenario { MissingLinkSchemeRule() }
    }
})

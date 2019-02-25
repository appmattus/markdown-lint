package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object FN001Test : Spek({
    Feature("FN001") {
        FileRuleScenario { FN001() }
    }
})

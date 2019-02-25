package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object MD039Test : Spek({
    Feature("MD039") {
        FileRuleScenario { MD039() }
    }
})

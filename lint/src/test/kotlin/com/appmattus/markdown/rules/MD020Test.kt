package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object MD020Test : Spek({
    Feature("MD020") {
        FileRuleScenario { MD020() }
    }
})

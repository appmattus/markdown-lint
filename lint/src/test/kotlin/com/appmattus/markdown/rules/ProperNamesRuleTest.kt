package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object ProperNamesRuleTest : Spek({
    Feature("ProperNamesRule") {
        FileRuleScenario(listOf("proper-names-projects.md")) {
            ProperNamesRule(
                names = listOf(
                    "GitHub",
                    "github.com",
                    "github.com/about",
                    "npm",
                    "NPM",
                    "Vuex",
                    "Vue",
                    "vue-router"
                ),
                codeBlocks = true
            )
        }

        FileRuleScenario(exclude = listOf("proper-names-projects.md")) { ProperNamesRule() }

    }
})

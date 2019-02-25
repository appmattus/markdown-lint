package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object MD044Test : Spek({
    Feature("MD044") {
        //FileRuleScenario(listOf("proper-names.md")) { MD044() }

        FileRuleScenario(listOf("proper-names-projects.md")) {
            MD044(
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

        FileRuleScenario(exclude = listOf("proper-names-projects.md")) { MD044() }

    }
})

package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object ProperNamesRuleTest : Spek({
    Feature("ProperNamesRule") {
        FileRuleScenario(listOf("proper-names-codeblocks.md")) { ProperNamesRule(codeBlocks = true) }

        FileRuleScenario(listOf("proper-names-ignore-codeblocks.md")) { ProperNamesRule(codeBlocks = false) }

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
                    "Vue.js",
                    "vue-router"
                ),
                codeBlocks = false
            )
        }

        FileRuleScenario(exclude = listOf("proper-names-projects.md")) { ProperNamesRule(codeBlocks = true) }

        FileRuleScenario(listOf("proper-names-files.md")) {
            ProperNamesRule(names = listOf("Gradle"), codeBlocks = false)
        }
    }
})

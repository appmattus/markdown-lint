package com.appmattus.markdown.rules

import kotlin.test.Test

class ProperNamesRuleTest {
    @Test
    fun `proper-names-codeblocks`() = fileRuleScenario(listOf("proper-names-codeblocks.md")) {
        ProperNamesRule(codeBlocks = true)
    }

    @Test
    fun `proper-names-ignore-codeblocks`() = fileRuleScenario(listOf("proper-names-ignore-codeblocks.md")) {
        ProperNamesRule(codeBlocks = false)
    }

    @Test
    fun `proper-names-projects`() = fileRuleScenario(listOf("proper-names-projects.md")) {
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

    @Test
    fun `all files except proper-names-projects`() = fileRuleScenario(exclude = listOf("proper-names-projects.md")) {
        ProperNamesRule(codeBlocks = true)
    }

    @Test
    fun `proper-names-files`() = fileRuleScenario(listOf("proper-names-files.md")) {
        ProperNamesRule(names = listOf("Gradle"), codeBlocks = false)
    }
}

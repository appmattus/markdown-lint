package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory

class ProperNamesRuleTest {

    @TestFactory
    fun properNamesRule() = FileTestFactory(
        allFiles + "proper-names-codeblocks.md", exclude = listOf("proper-names-projects.md")
    ) { ProperNamesRule(codeBlocks = true) }

    @TestFactory
    fun `properNamesRule ignore codeblocks`() =
        FileTestFactory(listOf("proper-names-ignore-codeblocks.md")) { ProperNamesRule(codeBlocks = false) }

    @TestFactory
    fun `properNamesRule projects`() = FileTestFactory(listOf("proper-names-projects.md")) {
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

    @TestFactory
    fun `properNamesRule files`() = FileTestFactory(listOf("proper-names-files.md")) {
        ProperNamesRule(names = listOf("Gradle"), codeBlocks = false)
    }
}

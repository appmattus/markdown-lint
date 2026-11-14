package com.appmattus.markdown.rules

import java.util.UUID
import kotlin.test.Test

class NoConsecutiveHyphensFilenameRuleTest {
    private val rule = { NoConsecutiveHyphensFilenameRule() }

    @Test
    fun `no consecutive hyphens in filename`() = filenameScenario(0, rule) {
        UUID.randomUUID().toString().lowercase()
    }

    @Test
    fun `2 hyphens in filename`() = filenameScenario(1, rule) {
        "hello--world.md"
    }

    @Test
    fun `3 hyphens in filename`() = filenameScenario(1, rule) {
        "hello---.md"
    }

    @Test
    fun `4 hyphens in filename`() = filenameScenario(1, rule) {
        "hello----.md"
    }

    @Test
    fun `2 hyphens twice in filename`() = filenameScenario(1, rule) {
        "hello--world--.md"
    }

    @Test
    fun `empty filename without extension`() = filenameScenario(0, rule) {
        ""
    }

    @Test
    fun `empty filename with markdown extension`() = filenameScenario(0, rule) {
        ".md"
    }
}

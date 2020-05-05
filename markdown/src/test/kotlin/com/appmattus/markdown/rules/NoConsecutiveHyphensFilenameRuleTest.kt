package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory
import java.util.UUID

class NoConsecutiveHyphensFilenameRuleTest {

    val rule = NoConsecutiveHyphensFilenameRule()

    @TestFactory
    fun `no consecutive hyphens in filename`() = FilenameTestFactory(0, rule) {
        UUID.randomUUID().toString().toLowerCase()
    }

    @TestFactory
    fun `2 hyphens in filename`() = FilenameTestFactory(1, rule) {
        "hello--world.md"
    }

    @TestFactory
    fun `3 hyphens in filename`() = FilenameTestFactory(1, rule) {
        "hello---.md"
    }

    @TestFactory
    fun `4 hyphens in filename`() = FilenameTestFactory(1, rule) {
        "hello----.md"
    }

    @TestFactory
    fun `2 hyphens twice in filename`() = FilenameTestFactory(1, rule) {
        "hello--world--.md"
    }

    @TestFactory
    fun `empty filename without extension`() = FilenameTestFactory(0, rule) {
        ""
    }

    @TestFactory
    fun `empty filename with markdown extension`() = FilenameTestFactory(0, rule) {
        ".md"
    }
}

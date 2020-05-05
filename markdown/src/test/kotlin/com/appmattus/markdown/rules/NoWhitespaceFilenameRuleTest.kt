package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory
import java.util.UUID

class NoWhitespaceFilenameRuleTest {
    val rule = NoWhitespaceFilenameRule()

    @TestFactory
    fun `no whitespace filename`() = FilenameTestFactory(0, rule) {
        UUID.randomUUID().toString().toLowerCase()
    }

    @TestFactory
    fun `whitespace in filename`() = FilenameTestFactory(1, rule) {
        val whitespace = listOf(" ", "\t", "\n", "\r").random()
        "hello${whitespace}world.md"
    }

    @TestFactory
    fun `whitespace in start of filename`() = FilenameTestFactory(1, rule) {
        val whitespace = listOf(" ", "\t", "\n", "\r").random()
        "${whitespace}helloworld.md"
    }

    @TestFactory
    fun `whitespace in filename before extension`() = FilenameTestFactory(1, rule) {
        val whitespace = listOf(" ", "\t", "\n", "\r").random()
        "hello-world$whitespace.md"
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

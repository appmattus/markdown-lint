package com.appmattus.markdown.rules

import com.flextrade.jfixture.JFixture
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
        val whitespace = JFixture().create().fromList(" ", "\t", "\n", "\r")
        "hello${whitespace}world.md"
    }

    @TestFactory
    fun `whitespace in start of filename`() = FilenameTestFactory(1, rule) {
        val whitespace = JFixture().create().fromList(" ", "\t", "\n", "\r")
        "${whitespace}helloworld.md"
    }

    @TestFactory
    fun `whitespace in filename before extension`() = FilenameTestFactory(1, rule) {
        val whitespace = JFixture().create().fromList(" ", "\t", "\n", "\r")
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

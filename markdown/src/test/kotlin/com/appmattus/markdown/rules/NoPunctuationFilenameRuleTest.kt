package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory
import java.util.UUID

class NoPunctuationFilenameRuleTest {
    val rule = NoPunctuationFilenameRule()

    @TestFactory
    fun `no punctuation in filename without extension`() = FilenameTestFactory(0, rule) {
        UUID.randomUUID().toString().toLowerCase()
    }

    @TestFactory
    fun `no punctuation in filename with extension`() = FilenameTestFactory(0, rule) {
        "${UUID.randomUUID().toString().toLowerCase()}.md"
    }

    @TestFactory
    fun `punctuation in filename`() = FilenameTestFactory(1, rule) {
        val whitespace = listOf("_", ".", "?").random()
        "hello${whitespace}world.md"
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

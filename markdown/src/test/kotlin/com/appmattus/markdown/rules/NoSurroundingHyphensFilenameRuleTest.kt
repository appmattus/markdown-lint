package com.appmattus.markdown.rules

import org.junit.jupiter.api.TestFactory

class NoSurroundingHyphensFilenameRuleTest {
    val rule = NoSurroundingHyphensFilenameRule()

    @TestFactory
    fun `no hyphens in filename`() = FilenameTestFactory(0, rule) {
        "abcdefgh.md"
    }

    @TestFactory
    fun `hyphen at start of filename`() = FilenameTestFactory(1, rule) {
        "-hello world.md"
    }

    @TestFactory
    fun `hyphen at end of filename with md extension`() = FilenameTestFactory(1, rule) {
        "hello world-.md"
    }

    @TestFactory
    fun `hyphen at end of filename with markdown extension`() = FilenameTestFactory(1, rule) {
        "hello world-.markdown"
    }

    @TestFactory
    fun `hyphen at end of filename without extension`() = FilenameTestFactory(1, rule) {
        "hello world-"
    }

    @TestFactory
    fun `hyphen at start and end of filename with extension`() = FilenameTestFactory(1, rule) {
        "-hello world-.md"
    }

    @TestFactory
    fun `hyphen at start and end of filename without extension`() = FilenameTestFactory(1, rule) {
        "-hello world-"
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

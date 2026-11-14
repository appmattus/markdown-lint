package com.appmattus.markdown.rules

import kotlin.test.Test

class NoSurroundingHyphensFilenameRuleTest {
    private val rule = { NoSurroundingHyphensFilenameRule() }

    @Test
    fun `no hyphens in filename`() = filenameScenario(0, rule) {
        "abcdefgh.md"
    }

    @Test
    fun `hyphen at start of filename`() = filenameScenario(1, rule) {
        "-hello world.md"
    }

    @Test
    fun `hyphen at end of filename with md extension`() = filenameScenario(1, rule) {
        "hello world-.md"
    }

    @Test
    fun `hyphen at end of filename with markdown extension`() = filenameScenario(1, rule) {
        "hello world-.markdown"
    }

    @Test
    fun `hyphen at end of filename without extension`() = filenameScenario(1, rule) {
        "hello world-"
    }

    @Test
    fun `hyphen at start and end of filename with extension`() = filenameScenario(1, rule) {
        "-hello world-.md"
    }

    @Test
    fun `hyphen at start and end of filename without extension`() = filenameScenario(1, rule) {
        "-hello world-"
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

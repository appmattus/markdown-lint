package com.appmattus.markdown.rules

import com.flextrade.jfixture.JFixture
import java.util.UUID
import kotlin.test.Test

class NoWhitespaceFilenameRuleTest {
    private val rule = { NoWhitespaceFilenameRule() }

    @Test
    fun `no whitespace filename`() = filenameScenario(0, rule) {
        UUID.randomUUID().toString().lowercase()
    }

    @Test
    fun `whitespace in filename`() = filenameScenario(1, rule) {
        val whitespace = if (System.getProperty("os.name").lowercase().contains("win")) {
            JFixture().create().fromList(" ")
        } else {
            JFixture().create().fromList(" ", "\t", "\n", "\r")
        }
        "hello${whitespace}world.md"
    }

    @Test
    fun `whitespace in start of filename`() = filenameScenario(1, rule) {
        val whitespace = if (System.getProperty("os.name").lowercase().contains("win")) {
            JFixture().create().fromList(" ")
        } else {
            JFixture().create().fromList(" ", "\t", "\n", "\r")
        }
        "${whitespace}helloworld.md"
    }

    @Test
    fun `whitespace in filename before extension`() = filenameScenario(1, rule) {
        val whitespace = if (System.getProperty("os.name").lowercase().contains("win")) {
            JFixture().create().fromList(" ")
        } else {
            JFixture().create().fromList(" ", "\t", "\n", "\r")
        }
        "hello-world$whitespace.md"
    }

    @Test
    fun `no whitespace filename 2`() = filenameScenario(0, rule) {
        UUID.randomUUID().toString().lowercase()
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

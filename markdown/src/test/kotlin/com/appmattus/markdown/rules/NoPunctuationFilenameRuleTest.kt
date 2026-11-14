package com.appmattus.markdown.rules

import com.flextrade.jfixture.JFixture
import java.util.UUID
import kotlin.test.Test

class NoPunctuationFilenameRuleTest {
    private val rule = { NoPunctuationFilenameRule() }

    @Test
    fun `no punctuation in filename without extension`() = filenameScenario(0, rule) {
        UUID.randomUUID().toString().lowercase()
    }

    @Test
    fun `no punctuation in filename with extension`() = filenameScenario(0, rule) {
        "${UUID.randomUUID().toString().lowercase()}.md"
    }

    @Test
    fun `punctuation in filename`() = filenameScenario(1, rule) {
        val whitespace = if (System.getProperty("os.name").lowercase().contains("win")) {
            JFixture().create().fromList("_", ".")
        } else {
            JFixture().create().fromList("_", ".", "?")
        }
        "hello${whitespace}world.md"
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

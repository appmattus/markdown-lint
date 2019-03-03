package com.appmattus.markdown.rules

import com.flextrade.jfixture.JFixture
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.util.UUID

object NoPunctuationFilenameRuleTest : Spek({
    Feature("NoPunctuationFilenameRule") {
        val rule = { NoPunctuationFilenameRule() }

        FilenameScenario("no punctuation in filename without extension", 0, rule) {
            UUID.randomUUID().toString().toLowerCase()
        }

        FilenameScenario("no punctuation in filename with extension", 0, rule) {
            "${UUID.randomUUID().toString().toLowerCase()}.md"
        }

        FilenameScenario("punctuation in filename", 1, rule) {
            val whitespace = JFixture().create().fromList("_", ".", "?")
            "hello${whitespace}world.md"
        }

        FilenameScenario("empty filename without extension", 0, rule) {
            ""
        }

        FilenameScenario("empty filename with markdown extension", 0, rule) {
            ".md"
        }
    }
})

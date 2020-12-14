package com.appmattus.markdown.rules

import com.flextrade.jfixture.JFixture
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.util.UUID

object NoWhitespaceFilenameRuleTest : Spek({
    Feature("NoWhitespaceFilenameRule") {
        val rule = { NoWhitespaceFilenameRule() }

        FilenameScenario("no whitespace filename", 0, rule) {
            UUID.randomUUID().toString().toLowerCase()
        }

        FilenameScenario("whitespace in filename", 1, rule) {
            val whitespace = if (System.getProperty("os.name").toLowerCase().contains("win")) {
                JFixture().create().fromList(" ")
            } else {
                JFixture().create().fromList(" ", "\t", "\n", "\r")
            }
            "hello${whitespace}world.md"
        }

        FilenameScenario("whitespace in start of filename", 1, rule) {
            val whitespace = if (System.getProperty("os.name").toLowerCase().contains("win")) {
                JFixture().create().fromList(" ", "\t")
            } else {
                JFixture().create().fromList(" ", "\t", "\n", "\r")
            }
            "${whitespace}helloworld.md"
        }

        FilenameScenario("whitespace in filename before extension", 1, rule) {
            val whitespace = if (System.getProperty("os.name").toLowerCase().contains("win")) {
                JFixture().create().fromList(" ")
            } else {
                JFixture().create().fromList(" ", "\t", "\n", "\r")
            }
            "hello-world$whitespace.md"
        }

        FilenameScenario("no whitespace filename", 0, rule) {
            UUID.randomUUID().toString().toLowerCase()
        }

        FilenameScenario("empty filename without extension", 0, rule) {
            ""
        }

        FilenameScenario("empty filename with markdown extension", 0, rule) {
            ".md"
        }
    }
})

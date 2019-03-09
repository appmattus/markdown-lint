package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.util.UUID

object NoConsecutiveHyphensFilenameRuleTest : Spek({
    Feature("NoConsecutiveHyphensFilenameRule") {
        val rule = { NoConsecutiveHyphensFilenameRule() }

        FilenameScenario("no consecutive hyphens in filename", 0, rule) {
            UUID.randomUUID().toString().toLowerCase()
        }

        FilenameScenario("2 hyphens in filename", 1, rule) {
            "hello--world.md"
        }

        FilenameScenario("3 hyphens in filename", 1, rule) {
            "hello---.md"
        }

        FilenameScenario("4 hyphens in filename", 1, rule) {
            "hello----.md"
        }

        FilenameScenario("2 hyphens twice in filename", 1, rule) {
            "hello--world--.md"
        }

        FilenameScenario("empty filename without extension", 0, rule) {
            ""
        }

        FilenameScenario("empty filename with markdown extension", 0, rule) {
            ".md"
        }
    }
})

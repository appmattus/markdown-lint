package com.appmattus.markdown.rules

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object NoSurroundingHyphensFilenameRuleTest : Spek({
    Feature("NoSurroundingHyphensFilenameRule") {
        val rule = { NoSurroundingHyphensFilenameRule() }

        FilenameScenario("no hyphens in filename", 0, rule) {
            "abcdefgh.md"
        }

        FilenameScenario("hyphen at start of filename", 1, rule) {
            "-hello world.md"
        }

        FilenameScenario("hyphen at end of filename with md extension", 1, rule) {
            "hello world-.md"
        }

        FilenameScenario("hyphen at end of filename with markdown extension", 1, rule) {
            "hello world-.markdown"
        }

        FilenameScenario("hyphen at end of filename without extension", 1, rule) {
            "hello world-"
        }

        FilenameScenario("hyphen at start and end of filename with extension", 1, rule) {
            "-hello world-.md"
        }

        FilenameScenario("hyphen at start and end of filename without extension", 1, rule) {
            "-hello world-"
        }

        FilenameScenario("empty filename without extension", 0, rule) {
            ""
        }

        FilenameScenario("empty filename with markdown extension", 0, rule) {
            ".md"
        }
    }
})

package com.appmattus.markdown.rules

import com.appmattus.markdown.errors.Error
import com.appmattus.markdown.loadDocumentUnixEol
import com.appmattus.markdown.loadDocumentWindowsEol
import com.appmattus.markdown.processing.MarkdownDocument
import mockDocument
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.style.gherkin.FeatureBody
import java.nio.file.Paths
import kotlin.test.fail

@Suppress("TestFunctionName")
fun FeatureBody.FileRuleScenario(
    files: List<String> = allFiles,
    exclude: List<String> = emptyList(),
    rules: () -> Rule
) {
    val rule = rules()

    files.toMutableList().apply {
        removeAll(exclude)
    }.forEach { filename ->
        val document = loadDocumentUnixEol(filename)
        val documentWindows = loadDocumentWindowsEol(filename)

        val expectedErrorCount =
            Regex("\\{${Regex.escape(rule::class.java.simpleName)}(:[0-9]+)?}").findAll(document.chars).count()

        documentScenario("$filename (unix eol)", rule, document, expectedErrorCount)
        documentScenario("$filename (windows eol)", rule, documentWindows, expectedErrorCount)
    }
}

private fun FeatureBody.documentScenario(
    scenarioName: String,
    rule: Rule,
    document: MarkdownDocument,
    expectedErrorCount: Int
) {
    Scenario(scenarioName) {
        lateinit var ruleErrors: List<Error>

        Given("the file $scenarioName") {}

        When("we run ${rule.javaClass.simpleName}") {
            ruleErrors = rule.processDocument(document)
        }

        Then("we expect $expectedErrorCount errors") {
            assertThat(ruleErrors.size).describedAs("Number of errors")
                .withFailMessage(ruleErrors.joinToString("\n")).isEqualTo(expectedErrorCount)
        }

        if (expectedErrorCount > 0) {
            And("the location of errors match") {
                val failures = mutableListOf<String>()
                ruleErrors.forEach { error ->
                    val errorRange =
                        IntRange(document.getLineNumber(error.startOffset), document.getLineNumber(error.endOffset))

                    val hasError = errorRange.any { line ->
                        document.lines[line].contains("{${rule::class.java.simpleName}}") ||
                                document.chars.contains("{${rule::class.java.simpleName}:${line + 1}}")
                    }

                    if (!hasError) {
                        println("Unexpected: $error")
                        failures.add(
                            document.chars.midSequence(
                                error.startOffset,
                                error.endOffset
                            ).toString()
                        )
                    }
                }
                if (failures.isNotEmpty()) {
                    fail(failures.joinToString())
                }
            }
        }
    }
}

@Suppress("TestFunctionName")
fun FeatureBody.FilenameScenario(description: String, errors: Int, rules: () -> Rule, filename: () -> String) {
    @Suppress("LocalVariableName")
    val _filename = filename()
    val mockDocument = mockDocument()

    val rule by memoized { rules() }

    Scenario(description) {
        lateinit var document: MarkdownDocument
        lateinit var ruleErrors: List<Error>

        Given("a document with filename \"$_filename\"") {
            document = MarkdownDocument(Paths.get(_filename), mockDocument)
        }

        When("we visit the document") {
            ruleErrors = rule.processDocument(document)
        }

        when (errors) {
            0 -> Then("we have no errors") {
                assertThat(ruleErrors).isEmpty()
            }
            1 -> Then("we have 1 error") {
                assertThat(ruleErrors).size().isOne
            }
            else -> Then("we have $errors errors") {
                assertThat(ruleErrors).size().isEqualTo(errors)
            }
        }
    }
}

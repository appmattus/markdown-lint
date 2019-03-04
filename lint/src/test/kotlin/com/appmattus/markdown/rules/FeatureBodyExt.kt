package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.loadDocument
import com.nhaarman.mockitokotlin2.mock
import com.vladsch.flexmark.util.ast.Document
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.style.gherkin.FeatureBody
import kotlin.test.fail

fun FeatureBody.FileRuleScenario(
    files: List<String> = allFiles,
    exclude: List<String> = emptyList(),
    rules: () -> Rule
) {
    val rule by memoized { rules() }

    files.toMutableList().apply {
        removeAll(exclude)
    }.forEach { filename ->
        val document = loadDocument(filename)
        val expectedErrorCount =
            Regex("\\{${Regex.escape(rule::class.java.simpleName)}(:[0-9]+)?}").findAll(document.chars).count()

        Scenario(filename) {
            Given("the file $filename") {}

            When("we run the ${rule.name} rule") {
                rule.visitDocument(document)
            }

            Then("we expect $expectedErrorCount errors") {
                assertThat(rule.errors.size).isEqualTo(expectedErrorCount).describedAs("Number of errors")
            }

            if (expectedErrorCount > 0) {
                And("the location of errors match") {
                    val failures = mutableListOf<String>()
                    rule.errors.forEach { error ->
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
}

fun FeatureBody.FilenameScenario(description: String, errors: Int, rules: () -> Rule, filename: () -> String) {
    val _filename = filename()
    val mockDocument = mock<Document>()
    val rule by memoized { rules() }

    Scenario(description) {
        lateinit var document: MarkdownDocument

        Given("a document with filename \"$_filename\"") {
            document = MarkdownDocument(_filename, mockDocument)
        }

        When("we visit the document") {
            rule.visitDocument(document)
        }

        if (errors == 0) {
            Then("we have no errors") {
                assertThat(rule.errors).isEmpty()
            }
        } else if (errors == 1) {
            Then("we have 1 error") {
                assertThat(rule.errors).size().isOne
            }
        } else {
            Then("we have $errors errors") {
                assertThat(rule.errors).size().isEqualTo(errors)
            }
        }
    }
}

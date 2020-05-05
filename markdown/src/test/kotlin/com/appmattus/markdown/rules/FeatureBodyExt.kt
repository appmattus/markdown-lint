package com.appmattus.markdown.rules

import com.appmattus.gherkin.dsl.And
import com.appmattus.gherkin.dsl.Gherkin
import com.appmattus.gherkin.dsl.Given
import com.appmattus.gherkin.dsl.Then
import com.appmattus.gherkin.dsl.When
import com.appmattus.gherkin.jupiter.gherkin
import com.appmattus.markdown.errors.Error
import com.appmattus.markdown.loadDocumentUnixEol
import com.appmattus.markdown.loadDocumentWindowsEol
import com.appmattus.markdown.processing.MarkdownDocument
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DynamicNode
import java.io.File
import kotlin.test.fail

@Suppress("TestFunctionName")
fun FileTestFactory(
    files: List<String> = allFiles,
    exclude: List<String> = emptyList(),
    rules: () -> Rule
): List<DynamicNode> {
    val rule = rules()

    return files.toMutableList().apply {
        removeAll(exclude)
    }.flatMap { filename ->
        val document = loadDocumentUnixEol(filename)
        val documentWindows = loadDocumentWindowsEol(filename)

        val expectedErrorCount =
            Regex("\\{${Regex.escape(rule::class.java.simpleName)}(:[0-9]+)?}").findAll(document.chars).count()

        listOf(
            Triple("$filename (unix eol)", document, expectedErrorCount),
            Triple("$filename (windows eol)", documentWindows, expectedErrorCount)
        )
    }.gherkin({ it.first }) { (filename, document, expectedErrorCount) ->
        document(filename, rule, document, expectedErrorCount)
    }
}

private fun Gherkin.document(
    scenarioName: String,
    rule: Rule,
    document: MarkdownDocument,
    expectedErrorCount: Int
) {
    val ruleErrors: List<Error>

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

@Suppress("TestFunctionName")
fun FilenameTestFactory(errors: Int, rule: Rule, filename: () -> String): Iterable<DynamicNode> = gherkin {
    @Suppress("LocalVariableName")
    val _filename = filename()
    val mockDocument = mockDocument()

    val document: MarkdownDocument
    val ruleErrors: List<Error>

    Given("a document with filename \"$_filename\"") {
        document = MarkdownDocument(File(_filename), mockDocument)
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

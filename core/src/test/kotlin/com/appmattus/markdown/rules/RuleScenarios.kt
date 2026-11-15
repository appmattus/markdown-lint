package com.appmattus.markdown.rules

import com.appmattus.markdown.errors.Error
import com.appmattus.markdown.loadDocumentUnixEol
import com.appmattus.markdown.loadDocumentWindowsEol
import com.appmattus.markdown.processing.MarkdownDocument
import emptyDocument
import org.assertj.core.api.Assertions.assertThat
import java.nio.file.Paths
import kotlin.test.fail

/**
 * Runs [rules] against each of [files] (minus [exclude]), with both unix and windows line endings, checking the errors
 * reported match the `{RuleName}` markers in each file. All failing files are reported together.
 */
fun fileRuleScenario(
    files: List<String> = allFiles,
    exclude: List<String> = emptyList(),
    rules: () -> Rule
) {
    val rule = rules()

    val failures = files.toMutableList().apply {
        removeAll(exclude)
    }.flatMap { filename ->
        val document = loadDocumentUnixEol(filename)
        val documentWindows = loadDocumentWindowsEol(filename)

        val expectedErrorCount =
            Regex("\\{${Regex.escape(rule::class.java.simpleName)}(:[0-9]+)?}").findAll(document.chars).count()

        listOfNotNull(
            documentScenario("$filename (unix eol)", rule, document, expectedErrorCount),
            documentScenario("$filename (windows eol)", rule, documentWindows, expectedErrorCount)
        )
    }

    if (failures.isNotEmpty()) {
        fail(failures.joinToString("\n\n"))
    }
}

/**
 * Returns a description of the failure, or null when the document produces the expected errors.
 */
private fun documentScenario(
    scenarioName: String,
    rule: Rule,
    document: MarkdownDocument,
    expectedErrorCount: Int
): String? {
    lateinit var ruleErrors: List<Error>

    // Given the file scenarioName

    // When we run the rule
    ruleErrors = rule.processDocument(document)

    // Then we expect expectedErrorCount errors
    if (ruleErrors.size != expectedErrorCount) {
        return "$scenarioName: expected $expectedErrorCount errors but was ${ruleErrors.size}\n" +
            ruleErrors.joinToString("\n")
    }

    // And the location of errors match
    val unexpected = ruleErrors.filterNot { error ->
        val errorRange =
            IntRange(document.getLineNumber(error.startOffset), document.getLineNumber(error.endOffset))

        errorRange.any { line ->
            document.lines[line].contains("{${rule::class.java.simpleName}}") ||
                document.chars.contains("{${rule::class.java.simpleName}:${line + 1}}")
        }
    }

    return if (unexpected.isEmpty()) {
        null
    } else {
        "$scenarioName: unexpected error locations\n" + unexpected.joinToString("\n") { error ->
            "$error: " + document.chars.midSequence(error.startOffset, error.endOffset)
        }
    }
}

/**
 * Runs [rules] against a document with the name returned by [filename], expecting [errors] errors.
 */
fun filenameScenario(errors: Int, rules: () -> Rule, filename: () -> String) {
    val rule = rules()

    lateinit var document: MarkdownDocument
    lateinit var ruleErrors: List<Error>

    // Given a document with filename
    document = MarkdownDocument(Paths.get(filename()), emptyDocument())

    // When we visit the document
    ruleErrors = rule.processDocument(document)

    // Then we have the expected number of errors
    assertThat(ruleErrors).size().isEqualTo(errors)
}

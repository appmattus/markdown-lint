package com.appmattus.markdown.rules

import com.appmattus.markdown.processing.MarkdownDocument
import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter

/**
 * # Multiple consecutive blank lines
 *
 * This rule is triggered when there are multiple consecutive blank lines in the document:
 *
 *     Some text here
 *
 *
 *     Some more text here
 *
 * To fix this, delete the offending lines:
 *
 *     Some text here
 *
 *     Some more text here
 *
 * Note: this rule will not be triggered if there are multiple consecutive blank lines inside code blocks.
 *
 * Based on [MD012](https://github.com/markdownlint/markdownlint/blob/master/lib/mdl/rules.rb)
 */
class NoMultipleBlanksRule(
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    override val description = "Multiple consecutive blank lines"

    private val regex = Regex("^\\s*(\r?\n|\n)\\s*(\r?\n|\n|$)", RegexOption.MULTILINE)

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        val codeBlocks = document.codeBlocks.map { IntRange(it.startLineNumber, it.endLineNumber) }

        regex.findAll(document.chars).map { match ->
            IntRange(match.range.start, match.range.endInclusive)
        }.filter { range ->
            (codeBlocks.find { it.contains(document.getLineNumber(range.start)) } == null)
        }.forEach { range ->
            errorReporter.reportError(range.start, range.endInclusive, description)
        }
    }
}

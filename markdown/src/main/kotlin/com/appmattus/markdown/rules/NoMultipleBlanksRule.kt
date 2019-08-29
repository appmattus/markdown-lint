package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.processing.MarkdownDocument

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

    private val description = "Replace multiple consecutive blank lines with a single blank line."

    private val regex = Regex("^\\s*(\r?\n|\n)\\s*(\r?\n|\n|$)", RegexOption.MULTILINE)

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        val codeBlocks = document.codeBlocks.map { IntRange(it.startLineNumber, it.endLineNumber) }

        regex.findAll(document.chars).map { match ->
            IntRange(match.range.first, match.range.last)
        }.filter { range ->
            (codeBlocks.find { it.contains(document.getLineNumber(range.first)) } == null)
        }.forEach { range ->
            errorReporter.reportError(range.first, range.last, description)
        }
    }
}

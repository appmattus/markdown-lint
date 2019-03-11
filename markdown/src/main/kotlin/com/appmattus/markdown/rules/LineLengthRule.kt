package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.processing.MarkdownDocument
import com.appmattus.markdown.rules.extentions.splitIntoLines

/**
 * # Line length
 *
 * This rule is triggered when there are lines that are longer than the configured line length (default: 80 characters).
 * To fix this, split the line up into multiple lines.
 *
 * This rule has an exception where there is no whitespace beyond the configured line length. This allows you to still
 * include items such as long URLs without being forced to break them in the middle.
 *
 * You also have the option to exclude this rule for code blocks and tables. To do this, set the [codeBlocks] and/or
 * [tables] parameters to false.
 *
 * Code blocks are included in this rule by default since it is often a requirement for document readability, and
 * tentatively compatible with code rules. Still, some languages do not lend themselves to short lines.
 *
 * Based on [MD013](https://github.com/markdownlint/markdownlint/blob/master/lib/mdl/rules.rb)
 */
class LineLengthRule(
    private val lineLength: Int = 80,
    private val codeBlocks: Boolean = true,
    private val tables: Boolean = true,
    private val headings: Boolean = true,
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    override val description = "Line length"
    override val tags = listOf("line_length")

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        var result = document.chars.splitIntoLines().filter {
            it.length > lineLength
        }.map {
            IntRange(it.startOffset + lineLength, it.endOffset)
        }

        // Remove links at the end of the file
        val links = document.allLinks.map { IntRange(it.startOffset, it.endOffset) }
        result = result.filter { range ->
            links.find {
                it.endInclusive == range.endInclusive && document.chars.getColumnAtIndex(it.start) <= lineLength
            } == null
        }

        if (!headings) {
            val codeBlocks = document.headings.map { IntRange(it.startLineNumber, it.endLineNumber) }
            result = result.filter { range ->
                (codeBlocks.find { it.contains(document.getLineNumber(range.start)) } == null)
            }
        }

        if (!codeBlocks) {
            val codeBlocks = document.codeBlocks.map { IntRange(it.startLineNumber, it.endLineNumber) }
            result = result.filter { range ->
                (codeBlocks.find { it.contains(document.getLineNumber(range.start)) } == null)
            }
        }

        if (!tables) {
            val tables = document.tables.map { IntRange(it.startLineNumber, it.endLineNumber) }
            result = result.filter { range ->
                (tables.find { it.contains(document.getLineNumber(range.start)) } == null)
            }
        }

        result.forEach { range ->
            errorReporter.reportError(range.start, range.endInclusive, description)
        }
    }
}

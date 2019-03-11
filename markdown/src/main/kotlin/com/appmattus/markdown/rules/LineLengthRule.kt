package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.processing.MarkdownDocument
import com.appmattus.markdown.rules.extentions.splitIntoLines
import com.vladsch.flexmark.ast.Emphasis
import com.vladsch.flexmark.ast.StrongEmphasis
import com.vladsch.flexmark.util.ast.Node

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
    punctuation: String = ".,;:!? ",
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    private val terminatingPunctuationRegex = Regex("[${Regex.escape(punctuation)}]?\\s*")

    override val description = "Line length"
    override val tags = listOf("line_length")

    data class LinkRef(val startOffset: Int, val endOffset: Int, val node: Node, val lineNumber: Int)

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        var result = document.chars.splitIntoLines().filter {
            it.length > lineLength
        }.map {
            IntRange(it.startOffset + lineLength, it.endOffset)
        }

        // Remove links at the end of the file
        val links = document.allLinks.map {

            var item: Node = it
            while (item.parent is Emphasis || item.parent is StrongEmphasis) {
                item = item.parent
            }

            LinkRef(item.startOffset, item.endOffset, item, document.getLineNumber(item.startOffset))
        }
        result = result.filter { range ->
            links.find { linkRef ->
                val lineNumber = document.getLineNumber(range.start)

                if (linkRef.lineNumber == lineNumber
                    && document.getColumnNumber(linkRef.startOffset) + 1 <= lineLength
                    && document.getColumnNumber(linkRef.endOffset - 1) + 2 >= lineLength
                ) {
                    val eol = document.chars.subSequence(linkRef.endOffset, document.chars.endOfLine(linkRef.endOffset))
                    eol.matches(terminatingPunctuationRegex)
                } else {
                    false
                }
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

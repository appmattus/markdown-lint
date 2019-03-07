package com.appmattus.markdown.rules

import com.appmattus.markdown.processing.MarkdownDocument
import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.vladsch.flexmark.util.sequence.BasedSequence

/**
 * # Fenced code blocks should be surrounded by blank lines
 *
 * This rule is triggered when fenced code blocks are either not preceded or not followed by a blank line:
 *
 *     Some text
 *     ```
 *     Code block
 *     ```
 *
 *     ```
 *     Another code block
 *     ```
 *     Some more text
 *
 * To fix this, ensure that all fenced code blocks have a blank line both before and after (except where the block is
 * at the beginning or end of the document):
 *
 *     Some text
 *
 *     ```
 *     Code block
 *     ```
 *
 *     ```
 *     Another code block
 *     ```
 *
 *     Some more text
 *
 * Rationale: Aside from aesthetic reasons, some parsers, including kramdown, will not parse fenced code blocks that
 * don't have blank lines before and after them.
 *
 * Based on [MD031](https://github.com/markdownlint/markdownlint/blob/master/lib/mdl/rules.rb)
 */
class BlanksAroundFencesRule(
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    override val description = "Fenced code blocks should be surrounded by blank lines"
    override val tags = listOf("code", "blank_lines")

    private val fenceRegEx = Regex("^(`{3,}|~{3,})")

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {

        var inCode = false
        var fence: String? = null

        val lines = document.lines.toMutableList().apply {
            add(document.lines.size, BasedSequence.NULL)
            add(0, BasedSequence.NULL)
        }.toList()

        lines.forEachIndexed { lineNum, line ->
            fenceRegEx.find(line.trim())?.let {
                if (!inCode || it.value.take(fence?.length ?: 0) == fence) {
                    fence = if (inCode) null else it.value
                    inCode = !inCode

                    if (surroundingLinesEmpty(inCode, lines, lineNum)) {
                        errorReporter.reportError(line.startOffset, line.endOffset, description)
                    }
                }
            }
        }
    }

    private fun surroundingLinesEmpty(inCode: Boolean, lines: List<BasedSequence>, lineNum: Int) =
        inCode && lines[lineNum - 1].isNotEmpty() || (!inCode && lines[lineNum + 1].isNotEmpty())
}

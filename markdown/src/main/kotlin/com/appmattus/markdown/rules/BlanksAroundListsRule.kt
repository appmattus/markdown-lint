package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.processing.MarkdownDocument
import com.vladsch.flexmark.util.sequence.BasedSequence

/**
 * # Lists should be surrounded by blank lines
 *
 * This rule is triggered when lists (of any kind) are either not preceded or not followed by a blank line:
 *
 *    Some text
 *    * Some
 *    * List
 *
 *    1. Some
 *    2. List
 *    Some text
 *
 * To fix this, ensure that all lists have a blank line both before and after (except where the block is at the
 * beginning or end of the document):
 *
 *    Some text
 *
 *    * Some
 *    * List
 *
 *    1. Some
 *    2. List
 *
 *    Some text
 *
 * Rationale: Aside from aesthetic reasons, some parsers, including kramdown, will not parse lists that don't have
 * blank lines before and after them.
 *
 * Note: List items without hanging indents are a violation of this rule; list items with hanging indents are okay:
 *
 *    * This is
 *    not okay
 *
 *    * This is
 *      okay
 *
 * Based on [MD032](https://github.com/markdownlint/markdownlint/blob/master/lib/mdl/rules.rb)
 */
class BlanksAroundListsRule(
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    private val fenceRegEx = Regex("^(`{3,}|~{3,})")
    private val listRegEx = Regex("^([*+\\-]|(\\d+\\.))\\s")
    private val emptyRegEx = Regex("^(\\s|$)")

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {

        var inList = false
        var inCode = false
        var fence: String? = null
        var prevLine: BasedSequence = BasedSequence.NULL

        val lines = document.lines

        var missingBlankAbove = false

        lines.forEach { line ->
            if (!inCode) {
                val listMarker = listRegEx.find(line.trim())?.value

                if (!inList && listMarker != null && !prevLine.contains(emptyRegEx)) {
                    missingBlankAbove = true
                } else if (inList && listMarker == null && !line.contains(emptyRegEx)) {

                    val description = if (missingBlankAbove) {
                        "Lists should be surrounded by blank lines. Missing blank above and below this line."
                    } else {
                        "Lists should be surrounded by blank lines. Missing blank below this line."
                    }

                    errorReporter.reportError(prevLine.startOffset, prevLine.endOffset, description)

                    missingBlankAbove = false
                } else if (missingBlankAbove) {
                    errorReporter.reportError(
                        prevLine.startOffset,
                        prevLine.endOffset,
                        "Lists should be surrounded by blank lines. Missing blank above this line."
                    )

                    missingBlankAbove = false
                }

                inList = listMarker != null
            }

            fenceRegEx.find(line.trim())?.let {
                if (!inCode || it.value.take(fence?.length ?: 0) == fence) {
                    fence = if (inCode) null else it.value
                    inCode = !inCode
                    inList = false
                }
            }
            prevLine = line
        }
    }
}

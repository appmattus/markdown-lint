package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.processing.MarkdownDocument
import com.vladsch.flexmark.ast.Code
import com.vladsch.flexmark.ast.FencedCodeBlock
import com.vladsch.flexmark.ast.IndentedCodeBlock

/**
 * # Spaces inside emphasis markers
 *
 * This rule is triggered when emphasis markers (bold, italic) are used, but they have spaces between the markers and
 * the text:
 *
 *     Here is some ** bold ** text.
 *
 *     Here is some * italic * text.
 *
 *     Here is some more __ bold __ text.
 *
 *     Here is some more _ italic _ text.
 *
 * To fix this, remove the spaces around the emphasis markers:
 *
 *     Here is some **bold** text.
 *
 *     Here is some *italic* text.
 *
 *     Here is some more __bold__ text.
 *
 *     Here is some more _italic_ text.
 *
 * Rationale: Emphasis is only parsed as such when the asterisks/underscores aren't completely surrounded by spaces.
 * This rule attempts to detect where they were surrounded by spaces, but it appears that emphasized text was intended
 * by the author.
 *
 * Based on [MD037](https://github.com/markdownlint/markdownlint/blob/master/lib/mdl/rules.rb)
 */
class NoSpaceInEmphasisRule(
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    private val startRegex = Regex("\\s(\\*\\*?|__?)\\s(.+)\\1")
    private val endRegex = Regex("(\\*\\*?|__?)(.+)\\s\\1\\s")

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {

        document.allText.filterNot {
            it.parent is FencedCodeBlock || it.parent is IndentedCodeBlock || it.parent is Code
        }.forEach {
            val startMatch = startRegex.find(it.chars)
            val endMatch = endRegex.find(it.chars)

            if (startMatch != null || endMatch != null) {
                val marker = startMatch?.groupValues?.get(1) ?: endMatch?.groupValues?.get(1)
                val content = (startMatch?.groupValues?.get(2) ?: endMatch?.groupValues?.get(2))!!.trim()
                val description = "Avoid spaces inside emphasis markers, for example change to " +
                        "'$marker$content$marker'."

                errorReporter.reportError(it.startOffset, it.endOffset, description)
            }
        }
    }
}

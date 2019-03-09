package com.appmattus.markdown.rules

import com.appmattus.markdown.processing.MarkdownDocument
import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter

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

    override val description = "Spaces inside emphasis markers"
    override val tags = listOf("whitespace", "emphasis")

    private val startRegex = Regex("\\s(\\*\\*?|__?)\\s.+\\1")
    private val endRegex = Regex("(\\*\\*?|__?).+\\s\\1\\s")

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {

        document.allText.forEach {
            if (it.chars.contains(startRegex) || it.chars.contains(endRegex)) {
                errorReporter.reportError(it.startOffset, it.endOffset, description)
            }
        }
    }
}

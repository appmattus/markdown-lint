package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.processing.MarkdownDocument
import com.vladsch.flexmark.ast.ListItem
import com.vladsch.flexmark.util.ast.Document

/**
 * # Multiple top level headers in the same document
 *
 * This rule is triggered when a top level header is in use (the first line of the file is a h1 header), and more than
 * one h1 header is in use in the document:
 *
 *     # Top level header
 *
 *     # Another top level header
 *
 * To fix, structure your document so that there is a single h1 header that is the title for the document, and all
 * later headers are h2 or lower level headers:
 *
 *     # Title
 *
 *     ## Header
 *
 *     ## Another header
 *
 * Rationale: A top level header is a h1 on the first line of the file, and serves as the title for the document. If
 * this convention is in use, then there can not be more than one title for the document, and the entire document
 * should be contained within this header.
 *
 * Note: The level parameter can be used to change the top level (ex: to h2) in cases where an h1 is added externally.
 *
 * Based on [MD025](https://github.com/markdownlint/markdownlint/blob/master/lib/mdl/rules.rb)
 */
class SingleH1Rule(
    private val level: Int = 1,
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    private val description = "Multiple top level $level headers in the same document, replace with a level " +
            "${level + 1} header. Configuration: level=$level."

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        val headers = document.headings.filterNot { it.parent is ListItem }.filter { it.level == level }

        if (headers.isNotEmpty() && headers[0].previous == null && headers[0].parent is Document) {
            headers.drop(1).forEach { heading ->
                errorReporter.reportError(heading.startOffset, heading.endOffset, description)
            }
        }
    }
}

package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.processing.MarkdownDocument
import com.appmattus.markdown.rules.extensions.indent

/**
 * Consider starting bulleted lists at the beginning of the line
 *
 * This rule is triggered when top level lists don't start at the beginning of a line:
 *
 *     Some text
 *
 *       * List item
 *       * List item
 *
 * To fix, ensure that top level list items are not indented:
 *
 *     Some test
 *
 *     * List item
 *     * List item
 *
 * Rationale: Starting lists at the beginning of the line means that nested list items can all be indented by the same
 * amount when an editor's indent function or the tab key is used to indent. Starting a list 1 space in means that the
 * indent of the first nested list is less than the indent of the second level (3 characters if you use 4 space tabs,
 * or 1 character if you use 2 space tabs).
 *
 * Note: This rule is triggered for the following scenario because the unordered sublist is not recognized as such by
 * the parser. Not being nested 3 characters as required by the outer ordered list, it creates a top-level unordered
 * list instead.
 *
 *     1. List item
 *       - List item
 *       - List item
 *     1. List item
 *
 * Based on [MD006](https://github.com/markdownlint/markdownlint/blob/master/lib/mdl/rules.rb)
 */
class UlStartLeftRule(
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        document.topLevelListBlocks.forEach {
            if (it.indent() != 0) {
                val description = "Start bullet list items at the start of the line, currently indented " +
                        "${it.indent()} characters."

                errorReporter.reportError(it.startOffset, it.endOffset, description)
            }
        }
    }
}

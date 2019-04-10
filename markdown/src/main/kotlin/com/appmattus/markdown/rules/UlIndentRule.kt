package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.processing.MarkdownDocument
import com.vladsch.flexmark.ast.BulletListItem
import com.vladsch.flexmark.ast.ListItem
import com.vladsch.flexmark.ast.OrderedListItem
import com.vladsch.flexmark.util.ast.Document

/**
 * # Unordered list indentation
 *
 * This rule is triggered when list items are not indented by the configured number of spaces (default: 2).
 *
 * Example:
 *
 *     * List item
 *        * Nested list item indented by 3 spaces
 *
 * Corrected Example:
 *
 *     * List item
 *       * Nested list item indented by 2 spaces
 *
 * Rationale (2 space indent): indenting by 2 spaces allows the content of a nested list to be in line with the start
 * of the content of the parent list when a single space is used after the list marker.
 *
 * Rationale (4 space indent): Same indent as code blocks, simpler for editors to implement. See
 * [Indentation of content inside lists](https://www.cirosantilli.com/markdown-style-guide/#indentation-of-content-inside-lists)
 * for more information.
 *
 * In addition, this is a compatibility issue with multi-markdown parsers, which require a 4 space indents. See
 * [Sub-lists not indenting](http://support.markedapp.com/discussions/problems/21-sub-lists-not-indenting) for a
 * description of the problem.
 *
 * Based on [MD007](https://github.com/markdownlint/markdownlint/blob/master/lib/mdl/rules.rb) and
 * [MD007](https://github.com/DavidAnson/markdownlint/blob/master/lib/md007.js)
 */
class UlIndentRule(
    private val indent: Int = 2,
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    override val description = "Unordered list indentation"

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        document.unorderedListItems.forEach {
            val parentListItem = it.parentListItemOrNull()

            var expectedMarkerPos = parentListItem?.let {
                document.chars.getColumnAtIndex(parentListItem.openingMarker.startOffset) + indent
            } ?: 0

            if (parentListItem is OrderedListItem) {
                // When contained in an ordered list, align with it's content
                expectedMarkerPos = document.chars.getColumnAtIndex(parentListItem.childChars.startOffset)
            }

            val actualMarkerPos = document.chars.getColumnAtIndex(it.openingMarker.startOffset)

            if (expectedMarkerPos != actualMarkerPos) {
                errorReporter.reportError(it.startOffset, it.endOffset, description)
            }
        }
    }

    fun BulletListItem.parentListItemOrNull(): ListItem? {
        var cur = parent
        while (cur !is Document) {
            cur = cur.parent
            if (cur is ListItem)
                return cur
        }
        return null
    }
}

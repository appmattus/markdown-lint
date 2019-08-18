package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.processing.MarkdownDocument
import com.vladsch.flexmark.ast.BulletList
import com.vladsch.flexmark.ast.ListItem
import com.vladsch.flexmark.ast.OrderedList

/**
 * # Spaces after list markers
 *
 * This rule checks for the number of spaces between a list marker (e.g. '-', '*', '+' or '1.') and the text of the
 * list item.
 *
 * The number of spaces checked for depends on the document style in use, but the default is 1 space after any list
 * marker:
 *
 *     * Foo
 *     * Bar
 *     * Baz
 *
 *     1. Foo
 *     1. Bar
 *     1. Baz
 *
 *     1. Foo
 *        * Bar
 *     1. Baz
 *
 * A document style may change the number of spaces after unordered list items and ordered list items independently,
 * as well as based on whether the content of every item in the list consists of a single paragraph, or multiple
 * paragraphs (including sub-lists and code blocks).
 *
 * For example, the style guide at [Spaces after list marker](https://www.cirosantilli.com/markdown-style-guide/#spaces-after-list-marker)
 * specifies that 1 space after the list marker should be used if every item in the list fits within a single paragraph,
 * but to use 2 or 3 spaces (for ordered and unordered lists respectively) if there are multiple paragraphs of content
 * inside the list:
 *
 *     * Foo
 *     * Bar
 *     * Baz
 *
 *     vs.
 *
 *     *   Foo
 *
 *         Second paragraph
 *
 *     *   Bar
 *
 *     or
 *
 *     1.  Foo
 *
 *         Second paragraph
 *
 *     1.  Bar
 *
 * To fix this, ensure the correct number of spaces are used after list marker for your selected document style.
 *
 * Based on [MD030](https://github.com/markdownlint/markdownlint/blob/master/lib/mdl/rules.rb)
 */
class ListMarkerSpaceRule(
    private val ulSingle: Int = 1,
    private val olSingle: Int = 1,
    private val ulMulti: Int = 1,
    private val olMulti: Int = 1,
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {

        document.listBlocks.forEach { listBlock ->
            val single = listBlock.children.all {
                it.firstChild == it.lastChild
            }

            val indent = when {
                listBlock is OrderedList && single -> olSingle
                listBlock is OrderedList && !single -> olMulti
                listBlock is BulletList && single -> ulSingle
                listBlock is BulletList && !single -> ulMulti
                else -> throw IllegalStateException()
            }

            listBlock.children.forEach { item ->
                item as ListItem

                // For task lists we look at the marker suffix as the opening content
                val startContent = item.markerSuffix.takeIf(CharSequence::isNotEmpty) ?: item.firstChild?.chars

                if (startContent != null && startContent.startOffset - item.openingMarker.endOffset != indent) {
                    val description = "Ensure $indent spaces after list marker. Configuration: ulSingle=$ulSingle, " +
                            "olSingle=$olSingle, ulMulti=$ulMulti, olMulti=$olMulti."

                    errorReporter.reportError(item.startOffset, item.endOffset, description)
                }
            }
        }
    }
}

package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.processing.MarkdownDocument
import com.appmattus.markdown.rules.extentions.indent
import com.vladsch.flexmark.ast.BulletListItem
import com.vladsch.flexmark.ast.ListItem
import com.vladsch.flexmark.ast.OrderedListItem
import com.vladsch.flexmark.util.ast.Node

/**
 * # Inconsistent indentation for list items at the same level
 *
 * This rule is triggered when list items are parsed as being at the same level, but don't have the same indentation:
 *
 *     * Item 1
 *         * Nested Item 1
 *         * Nested Item 2
 *        * A misaligned item
 *
 * Usually this rule will be triggered because of a typo. Correct the indentation for the list to fix it:
 *
 *     * Item 1
 *      * Nested Item 1
 *      * Nested Item 2
 *      * Nested Item 3
 *
 * Based on [MD005](https://github.com/markdownlint/markdownlint/blob/master/lib/mdl/rules.rb)
 */
class ListIndentRule(
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    override val description = "Inconsistent indentation for list items at the same level"

    private data class ItemLevel(val indentLevel: Int, var ordered: ItemLevel? = null, var unordered: ItemLevel? = null)

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {

        val bullets = document.listItems

        if (bullets.isEmpty()) {
            return
        }

        val root = ItemLevel(0)

        bullets.forEach { b ->

            val hierarchy = listItemHierarchy(b)

            var cur = root
            hierarchy.forEach {
                cur = when (it) {
                    is BulletListItem -> (cur.unordered ?: ItemLevel(it.indent())).also { cur.unordered = it }
                    is OrderedListItem -> (cur.ordered ?: ItemLevel(it.indent())).also { cur.ordered = it }
                    else -> throw IllegalStateException()
                }
            }

            if (cur.indentLevel != b.indent()) {
                errorReporter.reportError(b.startOffset, b.endOffset, description)
            }
        }
    }

    private fun listItemHierarchy(leafItem: ListItem) = generateSequence(leafItem as Node) {
        it.parent
    }.filterIsInstance(ListItem::class.java).toList().asReversed()
}

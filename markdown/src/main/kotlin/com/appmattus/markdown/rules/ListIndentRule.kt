package com.appmattus.markdown.rules

import com.appmattus.markdown.processing.MarkdownDocument
import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.rules.extentions.indent
import com.appmattus.markdown.rules.extentions.level

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
    override val tags = listOf("bullet", "ul", "indentation")

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {

        val bullets = document.listItems

        if (bullets.isEmpty()) {
            return
        }

        val indentLevels = mutableMapOf<Int, Int>()

        bullets.forEach { b ->

            val indentLevel = b.indent()

            if (!indentLevels.containsKey(b.level())) {
                indentLevels[b.level()] = indentLevel
            } else if (indentLevel != indentLevels[b.level()]) {
                errorReporter.reportError(b.startOffset, b.endOffset, description)
            }
        }
    }
}

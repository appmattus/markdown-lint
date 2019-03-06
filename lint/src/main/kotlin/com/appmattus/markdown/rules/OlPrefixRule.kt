package com.appmattus.markdown.rules

import com.appmattus.markdown.ErrorReporter
import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup
import com.appmattus.markdown.rules.config.OrderedListStyle
import com.appmattus.markdown.rules.extentions.index
import com.vladsch.flexmark.ast.OrderedList

/**
 * # Ordered list item prefix
 *
 * This rule is triggered on ordered lists that do not either start with '1.' or do not have a prefix that increases
 * in numerical order (depending on the configured style, which defaults to [OrderedListStyle.One]).
 *
 * Example valid list if the style is configured as [OrderedListStyle.One]:
 *
 *     1. Do this.
 *     1. Do that.
 *     1. Done.
 *
 * Example valid list if the style is configured as [OrderedListStyle.Ordered]:
 *
 *     1. Do this.
 *     2. Do that.
 *     3. Done.
 *
 * Based on [MD029](https://github.com/markdownlint/markdownlint/blob/master/lib/mdl/rules.rb)
 */
class OlPrefixRule(
    val style: OrderedListStyle = OrderedListStyle.One,
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    override val description = "Ordered list item prefix"
    override val tags = listOf("ol")

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        when (style) {
            OrderedListStyle.One -> {
                document.orderedListItems.forEach {
                    if (it.openingMarker.toString() != "1.") {
                        errorReporter.reportError(it.openingMarker.startOffset, it.openingMarker.endOffset, description)
                    }
                }
            }
            OrderedListStyle.Ordered -> {
                document.orderedListItems.forEach {
                    val startIndex = (it.parent as OrderedList).startNumber

                    if (it.openingMarker.toString() != "${startIndex + it.index()}.") {
                        errorReporter.reportError(it.openingMarker.startOffset, it.openingMarker.endOffset, description)
                    }
                }
            }
        }
    }
}

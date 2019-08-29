package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.processing.MarkdownDocument
import com.appmattus.markdown.rules.config.OrderedListStyle
import com.appmattus.markdown.rules.extensions.index
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
    private val style: OrderedListStyle = OrderedListStyle.One,
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        when (style) {
            OrderedListStyle.One -> {
                document.orderedListItems.forEach {
                    if (it.openingMarker.toString() != "1.") {
                        val description = "Ordered list item prefix expected '1.' but was '${it.openingMarker}'. " +
                                "Configuration: style=One."
                        errorReporter.reportError(it.openingMarker.startOffset, it.openingMarker.endOffset, description)
                    }
                }
            }
            OrderedListStyle.Ordered -> {
                document.orderedListItems.forEach {
                    val startIndex = (it.parent as OrderedList).startNumber

                    if (it.openingMarker.toString() != "${startIndex + it.index()}.") {
                        val description = "Ordered list item prefix expected '${startIndex + it.index()}.' but was " +
                                "'${it.openingMarker}'. Configuration: style=Ordered."
                        errorReporter.reportError(it.openingMarker.startOffset, it.openingMarker.endOffset, description)
                    }
                }
            }
        }
    }
}

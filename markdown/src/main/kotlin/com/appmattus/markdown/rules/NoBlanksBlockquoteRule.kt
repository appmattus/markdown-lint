package com.appmattus.markdown.rules

import com.appmattus.markdown.processing.MarkdownDocument
import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.vladsch.flexmark.ast.BlockQuote

/**
 * # Blank line inside blockquote
 *
 * This rule is triggered when two blockquote blocks are separated by nothing except for a blank line:
 *
 *     > This is a blockquote
 *     > which is immediately followed by
 *
 *     > this blockquote. Unfortunately
 *     > In some parsers, these are treated as the same blockquote.
 *
 * To fix this, ensure that any blockquotes that are right next to each other have some text in between:
 *
 *     > This is a blockquote.
 *
 *     And Jimmy also said:
 *
 *     > This too is a blockquote.
 *
 * Alternatively, if they are supposed to be the same quote, then add the blockquote symbol at the beginning of the
 * blank line:
 *
 *     > This is a blockquote.
 *     >
 *     > This is the same blockquote.
 *
 * Rationale: Some markdown parsers will treat two blockquotes separated by one or more blank lines as the same
 * blockquote, while others will treat them as separate blockquotes.
 *
 * Based on [MD028](https://github.com/markdownlint/markdownlint/blob/master/lib/mdl/rules.rb)
 */
class NoBlanksBlockquoteRule(
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    override val description = "Blank line inside blockquote"
    override val tags = listOf("blockquote", "whitespace")

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        document.blockQuotes.filter { it.next is BlockQuote }.forEach {
            errorReporter.reportError(it.startOffset, it.next.startOffset, description)
        }
    }
}

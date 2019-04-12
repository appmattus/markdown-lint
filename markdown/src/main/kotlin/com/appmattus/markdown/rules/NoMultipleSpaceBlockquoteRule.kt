package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.processing.MarkdownDocument
import com.vladsch.flexmark.ast.Paragraph

/**
 * # Multiple spaces after blockquote symbol
 *
 * This rule is triggered when blockquotes have more than one space after the blockquote (>) symbol:
 *
 *     >  This is a block quote with bad indentation
 *     >  there should only be one.
 *
 * To fix, remove any extraneous space:
 *
 *     > This is a blockquote with correct
 *     > indentation.
 *
 * Based on [MD027](https://github.com/markdownlint/markdownlint/blob/master/lib/mdl/rules.rb)
 */
class NoMultipleSpaceBlockquoteRule(
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        document.blockQuotes.map { it.firstChild as Paragraph }.forEach { paragraph ->
            paragraph.lineIndents.forEachIndexed { index, i ->
                if (i > 0) {
                    val item = paragraph.contentLines[index]
                    val description = "Multiple spaces after blockquote symbol, for example change to " +
                            "'> ${item.trim()}'."

                    errorReporter.reportError(item.startOffset, item.endOffset, description)
                }
            }
        }
    }
}

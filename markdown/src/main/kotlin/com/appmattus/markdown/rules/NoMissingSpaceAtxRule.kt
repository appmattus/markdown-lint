package com.appmattus.markdown.rules

import com.appmattus.markdown.processing.MarkdownDocument
import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.rules.config.HeaderStyle
import com.appmattus.markdown.rules.extentions.style

/**
 * # No space after hash on atx style header
 *
 * This rule is triggered when spaces are missing after the hash characters in an atx style header:
 *
 *     #Header 1
 *
 *     ##Header 2
 *
 * To fix this, separate the header text from the hash character by a single space:
 *
 *     # Header 1
 *
 *     ## Header 2
 *
 * Based on [MD018](https://github.com/markdownlint/markdownlint/blob/master/lib/mdl/rules.rb)
 */
class NoMissingSpaceAtxRule(
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    override val description = "No space after hash on atx style header"

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        document.headings.forEach { heading ->
            if (heading.style() == HeaderStyle.Atx) {
                if (heading.openingMarker.endOffset == heading.text.startOffset) {
                    errorReporter.reportError(heading.startOffset, heading.endOffset, description)
                }
            }
        }
    }
}

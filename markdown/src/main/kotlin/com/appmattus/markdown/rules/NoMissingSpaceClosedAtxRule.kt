package com.appmattus.markdown.rules

import com.appmattus.markdown.processing.MarkdownDocument
import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.rules.config.HeaderStyle
import com.appmattus.markdown.rules.extentions.style

/**
 * # No space inside hashes on closed atx style header
 *
 * This rule is triggered when spaces are missing inside the hash characters in a closed atx style header:
 *
 *     #Header 1#
 *
 *     ##Header 2##
 *
 * To fix this, separate the header text from the hash character by a single space:
 *
 *     # Header 1 #
 *
 *     ## Header 2 ##
 *
 * Note: this rule will fire if either side of the header is missing spaces.
 *
 * Based on [MD020](https://github.com/markdownlint/markdownlint/blob/master/lib/mdl/rules.rb)
 */
class NoMissingSpaceClosedAtxRule(
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    override val description = "No space inside hashes on closed atx style header"
    override val tags = listOf("headers", "atx_closed", "spaces")

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        document.headings.forEach { heading ->
            if (heading.style() == HeaderStyle.AtxClosed) {
                if (heading.openingMarker.endOffset == heading.text.startOffset ||
                    (heading.text.endOffset == heading.closingMarker.startOffset && !heading.text.endsWith("\\"))
                ) {
                    errorReporter.reportError(heading.startOffset, heading.endOffset, description)
                }
            }
        }
    }
}

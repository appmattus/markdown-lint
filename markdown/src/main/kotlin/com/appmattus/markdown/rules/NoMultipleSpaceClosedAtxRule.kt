package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.processing.MarkdownDocument
import com.appmattus.markdown.rules.config.HeaderStyle
import com.appmattus.markdown.rules.extensions.style

/**
 * # Multiple spaces inside hashes on closed atx style header
 *
 * This rule is triggered when more than one space is used to separate the header text from the hash characters in a
 * closed atx style header:
 *
 *     #  Header 1  #
 *
 *     ##  Header 2  ##
 *
 * To fix this, separate the header text from the hash character by a single space:
 *
 *     # Header 1 #
 *
 *     ## Header 2 ##
 *
 * Note: this rule will fire if either side of the header contains multiple spaces.
 *
 * Based on [MD021](https://github.com/markdownlint/markdownlint/blob/master/lib/mdl/rules.rb)
 */
class NoMultipleSpaceClosedAtxRule(
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        document.headings.forEach { heading ->
            if (heading.style() == HeaderStyle.AtxClosed) {
                if ((heading.text.startOffset - heading.openingMarker.endOffset > 1) ||
                    (heading.closingMarker.startOffset - heading.text.endOffset > 1)
                ) {
                    val description = "Multiple spaces inside hashes on closed atx style header, for example change " +
                            "to '${heading.openingMarker} ${heading.text} ${heading.closingMarker}'."
                    errorReporter.reportError(heading.startOffset, heading.endOffset, description)
                }
            }
        }
    }
}

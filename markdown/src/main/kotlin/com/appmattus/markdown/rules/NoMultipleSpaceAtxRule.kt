package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.processing.MarkdownDocument
import com.appmattus.markdown.rules.config.HeaderStyle
import com.appmattus.markdown.rules.extentions.style

/**
 * # Multiple spaces after hash on atx style header
 *
 * This rule is triggered when more than one space is used to separate the header text from the hash characters in an
 * atx style header:
 *
 *     #  Header 1
 *
 *     ##  Header 2
 *
 * To fix this, separate the header text from the hash character by a single space:
 *
 *     # Header 1
 *
 *     ## Header 2
 *
 * Based on [MD019](https://github.com/markdownlint/markdownlint/blob/master/lib/mdl/rules.rb)
 */
class NoMultipleSpaceAtxRule(
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        document.headings.forEach { heading ->
            if (heading.style() == HeaderStyle.Atx) {
                if (heading.text.startOffset - heading.openingMarker.endOffset > 1) {
                    val description = "Multiple spaces after hash on atx style header, for example change " +
                            "to '${heading.openingMarker} ${heading.text}'."
                    errorReporter.reportError(heading.startOffset, heading.endOffset, description)
                }
            }
        }
    }
}

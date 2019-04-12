package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.processing.MarkdownDocument

/**
 * # Header levels should only increment by one level at a time
 *
 * This rule is triggered when you skip header levels in a markdown document, for example:
 *
 *     # Header 1
 *
 *     ### Header 3
 *
 *     We skipped out a 2nd level header in this document
 *
 * When using multiple header levels, nested headers should increase by only one level at a time:
 *
 *     # Header 1
 *
 *     ## Header 2
 *
 *     ### Header 3
 *
 *     #### Header 4
 *
 *     ## Another Header 2
 *
 *     ### Another Header 3
 *
 * Based on [MD001](https://github.com/markdownlint/markdownlint/blob/master/lib/mdl/rules.rb)
 */
class HeaderIncrementRule(
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        var oldLevel: Int? = null

        document.headings.forEach { heading ->
            if (oldLevel?.let { heading.level > it + 1 } == true) {
                val description = "Header levels should only increment one level at a time. This should be a level " +
                        "${oldLevel!! + 1} header."
                errorReporter.reportError(heading.startOffset, heading.endOffset, description)
            }
            oldLevel = heading.level
        }
    }
}

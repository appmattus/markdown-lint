package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.processing.MarkdownDocument

/**
 * # Spaces inside link text
 *
 * This rule is triggered on links that have spaces surrounding the link text:
 *
 *     [ a link ](http://www.example.com/)
 *
 * To fix this, remove the spaces surrounding the link text:
 *
 *     [a link](http://www.example.com/)
 *
 * Based on [MD039](https://github.com/markdownlint/markdownlint/blob/master/lib/mdl/rules.rb)
 */
class NoSpaceInLinksRule(
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    private val openingMarkerRegex = "\\[\\s+".toRegex()
    private val closingMarkerRegex = "\\s+]".toRegex()

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        document.links.forEach {
            if (it.textOpeningMarker.endOffset != it.text.startOffset ||
                it.textClosingMarker.startOffset != it.text.endOffset
            ) {
                val replacement = it.chars
                    .replaceFirst(openingMarkerRegex, "[")
                    .replaceFirst(closingMarkerRegex, "]")

                val description = "Remove spaces inside link text, for example '$replacement'."

                errorReporter.reportError(it.startOffset, it.endOffset, description)
            }
        }
    }
}

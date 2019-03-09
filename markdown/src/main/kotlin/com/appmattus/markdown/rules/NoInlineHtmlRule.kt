package com.appmattus.markdown.rules

import com.appmattus.markdown.processing.MarkdownDocument
import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter

/**
 * # Inline HTML
 *
 * This rule is triggered whenever raw HTML is used in a markdown document:
 *
 *     <h1>Inline HTML header</h1>
 *
 * To fix this, use 'pure' markdown instead of including raw HTML:
 *
 *     # Markdown header
 *
 * Rationale: Raw HTML is allowed in markdown, but this rule is included for those who want their documents to only
 * include "pure" markdown, or for those who are rendering markdown documents in something other than HTML.
 *
 * Based on [MD033](https://github.com/markdownlint/markdownlint/blob/master/lib/mdl/rules.rb)
 */
class NoInlineHtmlRule(
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    override val description = "Inline HTML"
    override val tags = listOf("html")

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        document.htmlElements.forEach {
            errorReporter.reportError(it.startOffset, it.endOffset, description)
        }
    }
}

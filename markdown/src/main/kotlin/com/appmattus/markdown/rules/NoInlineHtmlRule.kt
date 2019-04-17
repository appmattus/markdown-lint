package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.processing.MarkdownDocument

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

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        document.htmlElements.forEach {
            @Suppress("MoveVariableDeclarationIntoWhen")
            val htmlTag = "^</?([a-zA-Z0-9]+)\\s*/?>".toRegex().find(it.chars)?.groupValues?.get(1)?.toLowerCase()

            val example = when (htmlTag) {
                "h1", "h2", "h3", "h4", "h5", "h6" -> {
                    val hashes = "#".repeat(htmlTag[1] - '1' + 1)
                    "Replace $htmlTag with a header, for example '$hashes Header'."
                }
                "p" -> "Replace <p> with a whitespace line between paragraphs."
                "b" -> "Replace <b> with strong emphasis, for example '**text**'."
                "a" -> "Replace <a> with a link, for example '<http://example.com>' or " +
                        "'[inline link](http://example.com)'."
                "u" -> "Replace <u> with emphasis or strong emphasis, for example '*text*' or '**text**'."
                "br" -> "Replace <br> with two trailing spaces at the end of a line, for example 'text  '."
                else -> ""
            }

            val description = "Replace inline HTML with plain markdown for better compatibility. $example"
            errorReporter.reportError(it.startOffset, it.endOffset, description)
        }
    }
}

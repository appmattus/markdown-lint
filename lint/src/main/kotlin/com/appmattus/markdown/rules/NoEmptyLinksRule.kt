package com.appmattus.markdown.rules

import com.appmattus.markdown.processing.MarkdownDocument
import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.rules.extentions.referenceUrl
import com.vladsch.flexmark.ast.LinkRef
import com.vladsch.flexmark.ast.Reference

/**
 * # No empty links
 *
 * This rule is triggered when an empty link is encountered:
 *
 *     [an empty link]()
 *
 * To fix the violation, provide a destination for the link:
 *
 *     [a valid link](https://example.com/)
 *
 * Empty fragments will trigger this rule:
 *
 *     [an empty fragment](#)
 *
 * But non-empty fragments will not:
 *
 *     [a valid fragment](#fragment)
 *
 * Based on [MD042](https://github.com/DavidAnson/markdownlint/blob/master/lib/md042.js)
 */
class NoEmptyLinksRule(
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    override val description = "No empty links"
    override val tags = listOf("links")

    private val emptyLinkRegex = Regex("#?|(?:<>)")

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        document.allLinks.forEach { link ->

            when (link) {
                is LinkRef -> link.referenceUrl()?.toString() ?: ""
                is Reference -> null
                else -> link.url.toString()
            }?.let { url ->
                if (url.matches(emptyLinkRegex)) {
                    errorReporter.reportError(link.startOffset, link.endOffset, description)
                }
            }
        }
    }
}

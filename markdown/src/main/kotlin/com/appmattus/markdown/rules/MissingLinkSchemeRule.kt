package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.processing.MarkdownDocument
import com.appmattus.markdown.rules.extentions.referenceUrl
import com.vladsch.flexmark.ast.LinkRef
import com.vladsch.flexmark.ast.Reference
import java.net.URI

/**
 * # Missing link scheme
 *
 * This rule is triggered when a link is missing the scheme:
 *
 *     [missing scheme link](www.example.com)
 *
 * To fix the violation, provide a scheme for the link:
 *
 *     [a valid link](https://example.com/)
 */
class MissingLinkSchemeRule(
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        document.allLinks.forEach { link ->

            when (link) {
                is LinkRef -> link.referenceUrl()
                is Reference -> null
                else -> link.url
            }?.let { url ->
                val uri = URI(url.toString())

                if (uri.scheme.isNullOrBlank() && uri.path.startsWith("www.")) {
                    val description = "Link is missing http/https scheme, for example 'https://${link.chars}'"

                    errorReporter.reportError(url.startOffset, url.endOffset, description)
                }
            }
        }
    }
}

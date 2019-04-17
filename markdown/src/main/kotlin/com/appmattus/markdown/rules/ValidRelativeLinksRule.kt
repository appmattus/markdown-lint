package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.processing.MarkdownDocument
import com.appmattus.markdown.rules.extentions.referenceUrl
import com.vladsch.flexmark.ast.LinkRef
import com.vladsch.flexmark.ast.Reference
import java.io.File
import java.net.URI

/**
 * # Relative links exist
 *
 * This rule is triggered when a relative link cannot be resolved:
 *
 *     [a relative link](this-file-does-not-exist.md)
 *
 * To fix the violation, ensure the linked file exists.
 */
class ValidRelativeLinksRule(
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        val parentDir = document.file.parent
        document.allLinks.forEach { link ->
            when (link) {
                is LinkRef -> link.referenceUrl()
                is Reference -> null
                else -> link.url
            }?.let { url ->
                val uri = URI(url.toString())

                if (uri.isRelative && uri.path.isNotEmpty() && !File(parentDir, uri.path).exists()) {
                    val expected = File(document.file.parent, uri.path).normalize().path
                    val description =
                        "Relative link does not exist, '${link.chars}', expected at '$expected'"
                    errorReporter.reportError(url.startOffset, url.endOffset, description)
                }
            }
        }
    }

    private val URI.isRelative
        get() = !isAbsolute && !path.startsWith("www.") && !path.startsWith("/")
}

package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.processing.MarkdownDocument
import com.appmattus.markdown.rules.extensions.isEmail
import java.net.URI
import java.nio.file.Files

/**
 * # Relative links exist
 *
 * This rule is triggered when a relative link cannot be resolved:
 *
 *     [a relative link](this-file-does-not-exist.md)
 *
 * To fix the violation, ensure the linked file exists.
 */
class ValidRelativeImagesRule(
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        val parentDir = document.file.parent
        document.allImageUrls.forEach { url ->
            val uri = URI(url.toString())

            if (uri.isRelative && uri.path.isNotEmpty() && !Files.exists(parentDir.resolve(uri.path))) {
                val expected = document.file.parent.resolve(uri.path).normalize().toString()
                val description =
                    "Relative image does not exist, '$url', expected at '$expected'"
                errorReporter.reportError(url.startOffset, url.endOffset, description)
            }
        }
    }

    private val URI.isRelative
        get() = !isAbsolute && !path.startsWith("www.") && !path.startsWith("/") && !path.isEmail
}

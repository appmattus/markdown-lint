package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.processing.MarkdownDocument

/**
 * # First header should be a top level header
 *
 * This rule is triggered when the first header in the document isn't a h1 header:
 *
 *     ## This isn't a H1 header
 *
 *     ### Another header
 *
 * The first header in the document should be a h1 header:
 *
 *     # Start with a H1 header
 *
 *     ## Then use a H2 for subsections
 *
 * Based on [MD002](https://github.com/markdownlint/markdownlint/blob/master/lib/mdl/rules.rb)
 */
class FirstHeaderH1Rule(
    private val level: Int = 1,
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    override val description = "First header should be a top level header"

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        document.headings.firstOrNull()?.let {
            if (it.level != level) {
                errorReporter.reportError(it.startOffset, it.endOffset, description)
            }
        }
    }
}

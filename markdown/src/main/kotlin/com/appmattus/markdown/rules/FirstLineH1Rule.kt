package com.appmattus.markdown.rules

import com.appmattus.markdown.processing.MarkdownDocument
import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter

/**
 * # First line in file should be a top level header
 *
 * This rule is triggered when the first line in the file isn't a top level (h1) header:
 *
 *     ```
 *     This is a file without a header
 *     ```
 *
 * To fix this, add a header to the top of your file:
 *
 *     ```
 *     # File with header
 *
 *     This is a file with a top level header
 *     ```
 *
 * Note: The [level] parameter can be used to change the top level (ex: to h2) in cases where an h1 is added externally.
 *
 * Based on [MD041](https://github.com/markdownlint/markdownlint/blob/master/lib/mdl/rules.rb)
 */
class FirstLineH1Rule(
    private val level: Int = 1,
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    override val description = "First line in file should be a top level header"
    override val tags = listOf("headers")

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        document.headings.firstOrNull().let { header ->
            if (header == null || header.lineNumber != 0 || header.level != level) {
                errorReporter.reportError(document.lines[0].startOffset, document.lines[0].endOffset, description)
            }
        }
    }
}

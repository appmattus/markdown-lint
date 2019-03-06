package com.appmattus.markdown.rules

import com.appmattus.markdown.ErrorReporter
import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup
import com.appmattus.markdown.rules.extentions.indent
import com.vladsch.flexmark.ast.ListItem

/**
 * # Headers must start at the beginning of the line
 *
 * This rule is triggered when a header is indented by one or more spaces:
 *
 *     Some text
 *
 *       # Indented header
 *
 * To fix this, ensure that all headers start at the beginning of the line:
 *
 *     Some text
 *
 *     # Header
 *
 * Rationale: Headers that don't start at the beginning of the line will not be parsed as headers, and will instead
 * appear as regular text.
 *
 * Based on [MD023](https://github.com/markdownlint/markdownlint/blob/master/lib/mdl/rules.rb)
 */
class HeaderStartLeftRule(
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    override val description = "Headers must start at the beginning of the line"
    override val tags = listOf("headers", "spaces")

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        document.headings.filterNot { it.parent is ListItem }.forEach { heading ->

            if (heading.indent() > 0) {
                errorReporter.reportError(heading.startOffset, heading.endOffset, description)
            }
        }
    }
}

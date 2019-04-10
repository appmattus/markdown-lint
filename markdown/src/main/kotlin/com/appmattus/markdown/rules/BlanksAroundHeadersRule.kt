package com.appmattus.markdown.rules

import com.appmattus.markdown.processing.MarkdownDocument
import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.vladsch.flexmark.ast.ListItem

/**
 * # Headers should be surrounded by blank lines
 *
 * This rule is triggered when headers (any style) are either not preceded or not followed by a blank line:
 *
 *     # Header 1
 *     Some text
 *
 *     Some more text
 *     ## Header 2
 *
 * To fix this, ensure that all headers have a blank line both before and after (except where the header is at the
 * beginning or end of the document):
 *
 *     # Header 1
 *
 *     Some text
 *
 *     Some more text
 *
 *     ## Header 2
 *
 * Rationale: Aside from aesthetic reasons, some parsers, including kramdown, will not parse headers that don't have a
 * blank line before, and will parse them as regular text.
 *
 * Based on [MD022](https://github.com/markdownlint/markdownlint/blob/master/lib/mdl/rules.rb)
 */
class BlanksAroundHeadersRule(
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    override val description = "Headers should be surrounded by blank lines"

    private val whitespaceRegex = Regex("\\s*")

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        document.headings.filterNot { it.parent is ListItem }.forEach { heading ->

            var isValid = true
            if (heading.startLineNumber > 0) {
                if (!document.lines[heading.startLineNumber - 1].matches(whitespaceRegex)) {
                    isValid = false
                }
            }

            //heading.startLineNumber
            if (heading.endLineNumber < document.lines.size - 2) {
                if (!document.lines[heading.endLineNumber + 1].matches(whitespaceRegex)) {
                    isValid = false
                }
            }

            if (!isValid) {
                errorReporter.reportError(heading.startOffset, heading.endOffset, description)
            }
        }
    }
}

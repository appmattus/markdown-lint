package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.processing.MarkdownDocument
import com.vladsch.flexmark.ast.ListItem

/**
 * # Multiple headers with the same content
 *
 * This rule is triggered if there are multiple headers in the document that have the same text:
 *
 *     # Some text
 *
 *     ## Some text
 *
 * To fix this, ensure that the content of each header is different:
 *
 *     # Some text
 *
 *     ## Some more text
 *
 * Rationale: Some markdown parses generate anchors for headers based on the header name, and having headers with the
 * same content can cause problems with this.
 *
 * If the parameter [allowDifferentNesting] is set to true, header duplication under different nesting is allowed, like
 * it usually happens in change logs:
 *
 *     # Change log
 *
 *     ## 2.0.0
 *
 *     ### Bug fixes
 *
 *     ### Features
 *
 *     ## 1.0.0
 *
 *     ### Bug fixes
 *
 * Based on [MD024](https://github.com/markdownlint/markdownlint/blob/master/lib/mdl/rules.rb)
 */
class NoDuplicateHeaderRule(
    private val allowDifferentNesting: Boolean = false,
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        if (!allowDifferentNesting) {
            val headingsMap = mutableListOf<String>()
            document.headings.filterNot { it.parent is ListItem }.forEach { heading ->
                if (headingsMap.contains(heading.text.toString())) {
                    val description = "Multiple headers with the same content, ${heading.text}. For changelogs " +
                            "consider setting allowDifferentNesting to true. Configuration: " +
                            "allowDifferentNesting=false."
                    errorReporter.reportError(heading.startOffset, heading.endOffset, description)
                } else {
                    headingsMap.add(heading.text.toString())
                }
            }
        } else {
            val stack: Array<MutableList<String>> = arrayOf(
                mutableListOf(),
                mutableListOf(),
                mutableListOf(),
                mutableListOf(),
                mutableListOf(),
                mutableListOf()
            )

            document.headings.filterNot { it.parent is ListItem }.forEach { heading ->
                val text = heading.text.toString()

                if ((0 until heading.level).any { stack[it].contains(text) }) {
                    val description = "Multiple headers with the same content, ${heading.text}. Configuration: " +
                            "allowDifferentNesting=true."
                    errorReporter.reportError(heading.startOffset, heading.endOffset, description)
                } else {
                    stack[heading.level - 1].add(text)
                    @Suppress("MagicNumber")
                    (heading.level until 6).forEach { stack[it] = mutableListOf() }
                }
            }
        }
    }
}

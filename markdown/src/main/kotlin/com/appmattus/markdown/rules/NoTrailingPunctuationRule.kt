package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.processing.MarkdownDocument
import com.vladsch.flexmark.ast.ListItem

/**
 * # Trailing punctuation in header
 *
 * This rule is triggered on any header that has a punctuation character as the last character in the line:
 *
 *     # This is a header.
 *
 * To fix this, remove any trailing punctuation:
 *
 *     # This is a header
 *
 * Note: The punctuation parameter can be used to specify what characters class as punctuation at the end of the
 * header. For example, you can set it to `'.,;:!'` to allow headers with question marks in them, such as might be used
 * in an FAQ.
 *
 * Based on [MD026](https://github.com/markdownlint/markdownlint/blob/master/lib/mdl/rules.rb)
 */
class NoTrailingPunctuationRule(
    private val punctuation: String = ".,;:!?",
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        document.headings.filterNot { it.parent is ListItem }.filter { heading ->
            punctuation.contains(heading.text.lastChar())
        }.forEach { heading ->
            val replacement = heading.text.replace("[${Regex.escape(punctuation)}]+$".toRegex(), "")
            val description = "Remove trailing punctuation in header, for example '$replacement'. Configuration: " +
                    "punctuation=$punctuation."

            errorReporter.reportError(heading.startOffset, heading.endOffset, description)
        }
    }
}

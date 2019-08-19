package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.processing.MarkdownDocument

/**
 * # Files should end with a single newline character
 *
 * This rule is triggered when there is not a single newline character at the end of a file.
 *
 * Example that triggers the rule:
 *
 *     # Heading
 *
 *     This file ends without a newline.[EOF]
 *
 * To fix the violation, add a newline character to the end of the file:
 *
 *     # Heading
 *
 *     This file ends with a newline.
 *     [EOF]
 *
 * Based on [MD047](https://github.com/DavidAnson/markdownlint/blob/master/lib/md047.js)
 */
class SingleTrailingNewlineRule(
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        val lines = document.lines

        if (lines.isNotEmpty()) {
            val lastLine = document.lines.last()

            if (lastLine.baseSubSequence(lastLine.endOffset).eolLength() == 0) {
                errorReporter.reportError(
                    lastLine.startOffset, lastLine.endOffset,
                    "Files should end with a single newline character"
                )
            }
        }
    }
}

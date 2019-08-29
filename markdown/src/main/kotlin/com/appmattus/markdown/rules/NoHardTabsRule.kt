package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.processing.MarkdownDocument
import com.appmattus.markdown.rules.extensions.indexOfAllList

/**
 * # Hard tabs
 *
 * This rule is triggered by any lines that contain hard tab characters instead of using spaces for indentation. To fix
 * this, replace any hard tab characters with spaces instead.
 *
 * Example:
 *
 *     Some text
 *
 *     	* hard tab character used to indent the list item
 *
 * Corrected example:
 *
 *     Some text
 *
 *         * Spaces used to indent the list item instead
 *
 * Based on [MD010](https://github.com/markdownlint/markdownlint/blob/master/lib/mdl/rules.rb)
 */
class NoHardTabsRule(
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    private val description = "Replace hard tab characters with spaces."

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        document.chars.indexOfAllList("\t").forEach {
            errorReporter.reportError(it, it + 1, description)
        }
    }
}

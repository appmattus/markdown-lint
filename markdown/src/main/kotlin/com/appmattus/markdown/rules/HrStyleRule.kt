package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.processing.MarkdownDocument
import com.appmattus.markdown.rules.config.HorizontalRuleStyle

/**
 * # Horizontal rule style
 *
 * This rule is triggered when inconsistent styles of horizontal rules are used in the document:
 *
 *     ---
 *
 *     - - -
 *
 *     ***
 *
 *     * * *
 *
 *     ****
 *
 * To fix this, ensure any horizontal rules used in the document are consistent, or match the given style if the rule
 * is so configured:
 *
 *     ---
 *
 *     ---
 *
 * Note: by default, this rule is configured to just require that all horizontal rules in the document are the same, and
 * will trigger if any of the horizontal rules are different than the first one encountered in the document. If you want
 * to configure the rule to match a specific style, the parameter given to the 'style' option is a string containing the
 * exact horizontal rule text that is allowed.
 *
 * Based on [MD035](https://github.com/markdownlint/markdownlint/blob/master/lib/mdl/rules.rb)
 */
class HrStyleRule(
    private val style: HorizontalRuleStyle = HorizontalRuleStyle.Dash,
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    override val description = "Horizontal rule style"

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        val hr = document.horizontalRules

        if (hr.isNotEmpty()) {
            val expectedChars = when (style) {
                is HorizontalRuleStyle.Consistent -> hr[0].chars.toString()
                is HorizontalRuleStyle.Exact -> style.chars
            }

            hr.forEach {
                if (it.chars.toString() != expectedChars) {
                    errorReporter.reportError(it.startOffset, it.endOffset, description)
                }
            }
        }
    }
}

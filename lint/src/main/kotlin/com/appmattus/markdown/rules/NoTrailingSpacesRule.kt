package com.appmattus.markdown.rules

import com.appmattus.markdown.ErrorReporter
import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup

/**
 * # Trailing spaces
 *
 * This rule is triggered on any lines that end with whitespace. To fix this, find the line that is triggered and
 * remove any trailing spaces from the end.
 *
 * The [brSpaces] parameter allows an exception to this rule for a specific amount of trailing spaces used to insert an
 * explicit line break/br element. For example, set [brSpaces] to 2 to allow exactly 2 spaces at the end of a line.
 *
 * Note: you have to set [brSpaces] to 2 or higher for this exception to take effect - you can't insert a br element
 * with just a single trailing space, so if you set [brSpaces] to 1, the exception will be disabled, just as if it was
 * set to the default of 0.
 *
 * Based on [MD009](https://github.com/markdownlint/markdownlint/blob/master/lib/mdl/rules.rb)
 */
class NoTrailingSpacesRule(
    private val brSpaces: Int = 2,
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    override val description = "Trailing spaces"
    override val tags = listOf("whitespace")

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        document.lines.forEach {
            if (it.endsWith(" ")) {
                val actual = it.length - it.trimEnd().length

                if (actual != 0 && actual != brSpaces) {
                    errorReporter.reportError(it.startOffset, it.endOffset, description)
                }
            }
        }
    }
}

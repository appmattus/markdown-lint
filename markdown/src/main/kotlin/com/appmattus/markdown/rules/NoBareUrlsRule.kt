package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.processing.MarkdownDocument

/**
 * # Bare URL used
 *
 * This rule is triggered whenever a URL is given that isn't surrounded by angle brackets:
 *
 *     For more information, see http://www.example.com/.
 *
 * To fix this, add angle brackets around the URL:
 *
 *     For more information, see <http://www.example.com/>.
 *
 * Rationale: Without angle brackets, the URL isn't converted into a link in many markdown parsers.
 *
 * Note: if you do want a bare URL without it being converted into a link, enclose it in a code block, otherwise in
 * some markdown parsers it will be converted:
 *
 *     `http://www.example.com`
 *
 * Based on [MD034](https://github.com/markdownlint/markdownlint/blob/master/lib/mdl/rules.rb)
 */
class NoBareUrlsRule(
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        document.autoLinks.forEach {
            if (it.openingMarker.isEmpty && it.closingMarker.isEmpty) {
                val description = "Wrap bare URL in angle brackets, for example '<${it.url}>'."
                errorReporter.reportError(it.startOffset, it.endOffset, description)
            }
        }
    }
}

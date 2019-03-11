package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.processing.MarkdownDocument
import com.appmattus.markdown.rules.config.HeaderStyle
import com.appmattus.markdown.rules.extentions.style

/**
 * # Header style
 *
 * This rule is triggered when different header styles (atx, setext, and 'closed' atx) are used in the same document:
 *
 *     # ATX style H1
 *
 *     ## Closed ATX style H2 ##
 *
 *     Setext style H1
 *     ===============
 *
 * Be consistent with the style of header used in a document:
 *
 *     # ATX style H1
 *
 *     ## ATX style H2
 *
 * The [HeaderStyle.SetextWithAtx] doc style allows atx-style headers of level 3 or more in documents with setext
 * style headers:
 *
 *     Setext style H1
 *     ===============
 *
 *     Setext style H2
 *     ---------------
 *
 *     ### ATX style H3
 *
 * Note: the configured header style can be a specific style to use ([HeaderStyle.Atx], [HeaderStyle.AtxClosed],
 * [HeaderStyle.Setext], [HeaderStyle.SetextWithAtx]), or simply require that the usage be [HeaderStyle.Consistent]
 * within the document.
 *
 * Based on [MD003](https://github.com/markdownlint/markdownlint/blob/master/lib/mdl/rules.rb)
 */
class ConsistentHeaderStyleRule(
    private val style: HeaderStyle = HeaderStyle.Atx,
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    override val description = "Header style"
    override val tags = listOf("headers")

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {

        val headings = document.headings
        if (headings.isEmpty()) {
            return
        }

        val docStyle = if (style == HeaderStyle.Consistent) headings.first().style() else style

        headings.forEach {
            if (docStyle == HeaderStyle.SetextWithAtx) {
                if (it.style() != HeaderStyle.Setext && !(it.style() == HeaderStyle.Atx && it.level > 2)) {
                    errorReporter.reportError(it.startOffset, it.endOffset, description)
                }
            } else {
                if (it.style() != docStyle) {
                    errorReporter.reportError(it.startOffset, it.endOffset, description)
                }
            }
        }
    }
}

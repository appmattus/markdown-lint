package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.processing.MarkdownDocument
import com.vladsch.flexmark.ast.Image
import com.vladsch.flexmark.ast.ImageRef

/**
 * # Images should have alternate text (alt text)
 *
 * This rule is triggered when an image is missing alternate text (alt text) information. Alternate text is important
 * for accessibility, describing the content of an image for people who may not be able to see it.
 *
 * Alternate text is commonly specified inline as:
 *
 *     ![Alternate text](image.jpg)
 *
 * Or with reference syntax as:
 *
 *     ![Alternate text][ref]
 *     ...
 *     [ref]: image.jpg "Optional title"
 *
 * Guidance for writing alternate text is available from the [W3C](https://www.w3.org/WAI/alt/),
 * [Wikipedia](https://en.wikipedia.org/wiki/Alt_attribute), and
 * [other locations](https://www.phase2technology.com/blog/no-more-excuses-definitive-guide-alt-text-field).
 *
 * Based on [MD045](https://github.com/DavidAnson/markdownlint/blob/master/lib/md045.js)
 */
class NoAltTextRule(
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    private val description = "Images should have alternate text (alt text)."

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        document.allImages.forEach {
            val altText = when (it) {
                is Image -> it.text
                is ImageRef -> it.text
                else -> null
            }

            if (altText.isNullOrBlank()) {
                errorReporter.reportError(it.startOffset, it.endOffset, description)
            }
        }
    }
}

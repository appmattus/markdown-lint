package com.appmattus.markdown.rules

import com.appmattus.markdown.processing.MarkdownDocument
import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.vladsch.flexmark.ast.Emphasis
import com.vladsch.flexmark.ast.StrongEmphasis
import com.vladsch.flexmark.ast.Text

/**
 * # Emphasis used instead of a header
 *
 * This check looks for instances where emphasized (i.e. bold or italic) text is used to separate sections, where a
 * header should be used instead:
 *
 *     **My document**
 *
 *     Lorem ipsum dolor sit amet...
 *
 *     _Another section_
 *
 *     Consectetur adipiscing elit, sed do eiusmod.
 *
 * To fix this, use markdown headers instead of emphasized text to denote sections:
 *
 *     # My document
 *
 *     Lorem ipsum dolor sit amet...
 *
 *     ## Another section
 *
 *     Consectetur adipiscing elit, sed do eiusmod.
 *
 * Note: this rule looks for single line paragraphs that consist entirely of emphasized text. It won't fire on emphasis
 * used within regular text, multi-line emphasized paragraphs, and paragraphs ending in punctuation. You can configure
 * what characters are recognized as punctuation.
 *
 * Based on [MD036](https://github.com/markdownlint/markdownlint/blob/master/lib/mdl/rules.rb)
 */
class NoEmphasisAsHeaderRule(
    private val punctuation: String = ".,;:!?",
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    override val description = "Emphasis used instead of a header"
    override val tags = listOf("headers", "emphasis")

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {

        document.topLevelParagraphs.filter { it.lineCount == 1 }.filter { it.firstChild == it.lastChild }.forEach {
            val wrapper = it.firstChild
            if (wrapper is Emphasis || wrapper is StrongEmphasis) {
                // Ensure there is only a single Text element
                if (wrapper.firstChild == wrapper.lastChild && wrapper.firstChild is Text) {
                    // Check sentence doesn't end in punctuation
                    if (!punctuation.contains(wrapper.firstChild.chars.lastChar())) {
                        errorReporter.reportError(wrapper.startOffset, wrapper.endOffset, description)
                    }
                }
            }
        }
    }
}

package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.processing.MarkdownDocument
import com.appmattus.markdown.rules.config.EmphasisStyle
import com.vladsch.flexmark.ast.DelimitedNode
import com.vladsch.flexmark.ast.Emphasis

/**
 * # Emphasis marker style
 *
 * This rule is triggered when the symbols used in the document for italic and bold text do not match the configured
 * emphasis marker style:
 *
 *     *Italic*
 *     _Also italic_
 *
 * To fix this issue, use the configured style for emphasis markers throughout the document:
 *
 *     *Italic*
 *     *Also italic*
 *
 * Note: the configured emphasis marker style can be a specific symbol ([EmphasisStyle.Asterisk],
 * [EmphasisStyle.Underscore]), or simply require that the usage be [EmphasisStyle.Consistent] within the document.
 */
class ConsistentEmphasisStyleRule(
    private val style: EmphasisStyle = EmphasisStyle.Asterisk,
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {

        val allEmphasis = document.allEmphasis

        if (allEmphasis.isEmpty()) {
            return
        }

        val docStyle = if (style == EmphasisStyle.Consistent) allEmphasis.first().style() else style

        allEmphasis.forEach {
            if (it.style() != docStyle) {
                val type = if (it is Emphasis) "Italic" else "Bold"

                val symbolCount = if (it is Emphasis) 1 else 2
                val expected = docStyle.symbol().repeat(symbolCount)
                val actual = it.style().symbol().repeat(symbolCount)

                val description = "$type text expected to use '$expected' emphasis markers but is '$actual', for " +
                        "example $expected${it.text}$expected. Configuration: style=${style.description()}."

                errorReporter.reportError(it.startOffset, it.endOffset, description)
            }
        }
    }

    private fun DelimitedNode.style(): EmphasisStyle {
        return when {
            openingMarker.contains('*') -> EmphasisStyle.Asterisk
            openingMarker.contains('_') -> EmphasisStyle.Underscore
            else -> throw IllegalStateException()
        }
    }

    private fun EmphasisStyle.symbol(): String {
        return when (this) {
            EmphasisStyle.Asterisk -> "*"
            EmphasisStyle.Underscore -> "_"
            else -> throw IllegalStateException()
        }
    }

    private fun EmphasisStyle.description(): String {
        return when (this) {
            EmphasisStyle.Asterisk -> "Asterisk '*'"
            EmphasisStyle.Underscore -> "Underscore '_'"
            EmphasisStyle.Consistent -> "Consistent"
        }
    }
}

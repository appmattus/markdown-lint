package com.appmattus.markdown.rules

import com.appmattus.markdown.processing.MarkdownDocument
import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.vladsch.flexmark.ast.AutoLink
import com.vladsch.flexmark.ast.Code
import com.vladsch.flexmark.ast.Link
import com.vladsch.flexmark.util.sequence.BasedSequence
import org.nibor.autolink.LinkExtractor

/**
 * # Proper names should have the correct capitalization
 *
 * This rule is triggered when any of the strings in the names array do not have the specified capitalization. It can
 * be used to enforce a standard letter case for the names of projects and products.
 *
 * For example, the language "JavaScript" is usually written with both the 'J' and 'S' capitalized - though sometimes
 * the 's' or 'j' appear in lower-case. To enforce the proper capitalization, specify the desired letter case in the
 * [names] parameter:
 *
 *     listOf("JavaScript")
 *
 * Set the [codeBlocks] parameter to false to disable this rule for code blocks.
 *
 * Based on [MD044](https://github.com/DavidAnson/markdownlint/blob/master/lib/md044.js)
 */
class ProperNamesRule(
    private val names: List<String> = listOf(
        "markdownlint",
        "JavaScript",
        "Node.js",
        "GitHub",
        "npm",
        "Internet Explorer"
    ),
    private val codeBlocks: Boolean = false,
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    override val description = "Proper names should have the correct capitalization"

    private val linkExtractor = LinkExtractor.builder().build()

    // Order of the names matters
    private val escapedList = names.sortedDescending().joinToString(separator = "|") { Regex.escape(it) }
    private val regex = Regex("\\S*\\b($escapedList)\\b\\S*", setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        val elements = mutableListOf<BasedSequence>()

        elements.addAll(
            document.allText
                .filterNot { it.parent is Link || it.parent is Code || it.parent is AutoLink }
                .map { it.chars }
        )


        elements.addAll(
            document.links
                .filterNot { it.text.isLink() }
                .map { it.text }
        )

        if (!codeBlocks) {
            elements.addAll(document.inlineCode.map { it.text })
            elements.addAll(document.codeBlocks.map { it.chars })
        }

        elements.forEach { text ->

            regex.findAll(text).map { match ->
                val range = match.groups[1]!!.range
                text.subSequence(range.start, range.endInclusive + 1)
            }.filter {
                !names.contains(it.toString())
            }.forEach {
                errorReporter.reportError(it.startOffset, it.endOffset, description)
            }
        }
    }

    private fun BasedSequence.isLink(): Boolean {
        return linkExtractor.extractLinks(this).any() ||
                !this.startsWithIgnoreCase("www.") && linkExtractor.extractLinks("www.${this}").any()
    }
}

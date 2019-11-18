package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.processing.MarkdownDocument
import com.vladsch.flexmark.ast.AutoLink
import com.vladsch.flexmark.ast.Code
import com.vladsch.flexmark.ast.FencedCodeBlock
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
 * Set the [codeBlocks] parameter to true to enable this rule for code blocks.
 *
 * Based on [MD044](https://github.com/DavidAnson/markdownlint/blob/master/lib/md044.js)
 */
class ProperNamesRule(
    private val names: List<String> = DefaultNames,
    private val codeBlocks: Boolean = false,
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    private val linkExtractor = LinkExtractor.builder().build()

    // Order of the names matters
    private val escapedList = names.sortedDescending().joinToString(separator = "|") { Regex.escape(it) }
    private val notWordChar = "[^\\pL\\pM\\p{Nd}\\p{Nl}\\p{Pc}[\\p{InEnclosedAlphanumerics}&&\\p{So}]]"
    private val regex = Regex(
        "(^|[\\s\"'(])($escapedList)([\\s\"')]|[.,;:!?]($notWordChar|$)|$)",
        setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE)
    )

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        val elements = mutableListOf<BasedSequence>()

        elements.addAll(
            document.allText
                .filterNot {
                    it.parent is Link || it.parent is Code || it.parent is AutoLink || it.parent is FencedCodeBlock
                }
                .map { it.chars }
        )

        elements.addAll(
            document.links
                .filterNot { it.text.isLink() }
                .map { it.text }
        )

        if (codeBlocks) {
            elements.addAll(document.inlineCode.map { it.text })
            elements.addAll(document.codeBlocks.map { it.contentChars })
        }

        elements.forEach { text ->

            regex.findAll(text).map { match ->
                val range = match.groups[2]!!.range
                text.subSequence(range.first, range.last + 1)
            }.filter {
                !names.contains(it.toString())
            }.forEach {
                val actual = it.toString()
                val expected = names.find { name ->
                    name.equals(actual, ignoreCase = true)
                }

                val description = "Proper names should have the correct capitalization, for example '$expected' " +
                        "instead of '$actual'. Configuration: codeBlocks=$codeBlocks, names=[…]."
                errorReporter.reportError(it.startOffset, it.endOffset, description)
            }
        }
    }

    private fun BasedSequence.isLink(): Boolean {
        return linkExtractor.extractLinks(this).any() ||
                !this.startsWithIgnoreCase("www.") && linkExtractor.extractLinks("www.$this").any()
    }

    companion object {
        val DefaultNames = listOf(
            "markdownlint",
            "JavaScript",
            "Node.js",
            "GitHub",
            "npm",
            "Internet Explorer",
            "Google Chrome",
            "Firefox",
            "Java",
            "Android Studio",
            "IntelliJ IDEA",
            "IntelliJ",
            "Kotlin",
            "API",
            "APIs",
            "SDK",
            "SDKs",
            "URL",
            "URLs",
            "JUnit",
            "APK",
            "AAR",
            "Gradle",
            "Gradle Enterprise",
            "Dagger",
            "Android",
            "Lint",
            "Artifactory",
            "Bintray",
            "Git",
            "Jenkins",
            "CircleCI",
            "Travis"
        )
    }
}

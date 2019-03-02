package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup
import com.vladsch.flexmark.ast.AutoLink
import com.vladsch.flexmark.ast.Code
import com.vladsch.flexmark.ast.Link
import com.vladsch.flexmark.util.sequence.BasedSequence
import org.nibor.autolink.LinkExtractor

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
) : Rule("ProperNames") {

    override val description = "Proper names should have the correct capitalization"
    override val tags = listOf("spelling")

    private val linkExtractor = LinkExtractor.builder().build()

    // Order of the names matters
    private val escapedList = names.sortedDescending().joinToString(separator = "|") { Regex.escape(it) }
    private val regex = Regex("\\S*\\b($escapedList)\\b\\S*", setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))

    override fun visitDocument(document: MarkdownDocument) {
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
                //it.toString() != name
            }.forEach {
                reportError(it.startOffset, it.endOffset, description)
            }
        }
    }

    private fun BasedSequence.isLink(): Boolean {
        return linkExtractor.extractLinks(this).any() ||
                !this.startsWithIgnoreCase("www.") && linkExtractor.extractLinks("www.${this}").any()
    }
}

/*
module.exports = {
    "names": [ "MD044", "proper-names" ],
    "description": "Proper names should have the correct capitalization",
    "tags": [ "spelling" ],
    "function": function MD044(params, onError) {
        const names = params.config.names || [];
        const codeBlocks = params.config.code_blocks;
        const includeCodeBlocks = (codeBlocks === undefined) ? true : !!codeBlocks;
        names.forEach(function forName(name) {
            const escapedName = shared.escapeForRegExp(name);
            const namePattern = "\\S*\\b(" + escapedName + ")\\b\\S*";
            const anyNameRe = new RegExp(namePattern, "gi");
            function forToken(token) {
                const fenceOffset = (token.type === "fence") ? 1 : 0;
                token.content.split(shared.newLineRe)
                    .forEach(function forLine(line, index) {
                        let match = null;
                        while ((match = anyNameRe.exec(line)) !== null) {
                            const fullMatch = match[0];
                            if (!shared.bareUrlRe.test(fullMatch)) {
                                const wordMatch = fullMatch
                                        .replace(/^\W*===/, "").replace(/\W*$/, "");
                                if (names.indexOf(wordMatch) === -1) {
                                    const lineNumber = token.lineNumber + index + fenceOffset;
                                    const range = [ match.index + 1, wordMatch.length ];
                                    shared.addErrorDetailIf(onError, lineNumber,
                                        name, match[1], null, range);
                                }
                            }
                        }
                    });
            }
            shared.forEachInlineChild(params, "text", forToken);
            if (includeCodeBlocks) {
                shared.forEachInlineChild(params, "code_inline", forToken);
                shared.filterTokens(params, "code_block", forToken);
                shared.filterTokens(params, "fence", forToken);
            }
        });
    }
};
 */

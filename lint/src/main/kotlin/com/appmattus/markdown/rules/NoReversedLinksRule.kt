package com.appmattus.markdown.rules

import com.appmattus.markdown.ErrorReporter
import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup

class NoReversedLinksRule(override val config: RuleSetup.Builder.() -> Unit = {}) : Rule("NoReversedLinks") {

    override val description = "Reversed link syntax"
    override val tags = listOf("links")

    private val regex = Regex("\\([^)]+\\)$")

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        document.linkRefs.forEach { linkRef ->
            if (linkRef.url.isEmpty) {
                linkRef.previous?.let {
                    regex.find(it.chars)?.let { match ->
                        errorReporter.reportError(it.startOffset + match.range.start, linkRef.endOffset, description)
                    } ?: errorReporter.reportError(linkRef.startOffset, linkRef.endOffset, description)
                } ?: errorReporter.reportError(linkRef.startOffset, linkRef.endOffset, description)
            }
        }
    }
}

/*
rule "MD011", "Reversed link syntax" do
  tags :links
  aliases 'no-reversed-links'
  check do |doc|
    doc.matching_text_element_lines(/\([^)]+\)\[[^\]]+\]/)
  end
end
 */

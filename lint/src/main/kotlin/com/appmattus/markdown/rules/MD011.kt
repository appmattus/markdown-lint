package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule

class MD011 : Rule("NoReversedLinks") {
    override val description = "Reversed link syntax"
    override val tags = listOf("links")

    private val regex = Regex("\\([^)]+\\)$")

    override fun visitDocument(document: MarkdownDocument) {
        document.linkRefs.forEach { linkRef ->
            if (linkRef.url.isEmpty) {
                linkRef.previous?.let {
                    regex.find(it.chars)?.let { match ->
                        reportError(it.startOffset + match.range.start, linkRef.endOffset, description)
                    } ?: reportError(linkRef.startOffset, linkRef.endOffset, description)
                } ?: reportError(linkRef.startOffset, linkRef.endOffset, description)
            }
        }
        /*document.linkRefs.forEach { linkRef ->
            if (linkRef.chars.startsWith("[") && linkRef.chars.endsWith("]")) {
                linkRef.previous?.let {
                    regex.find(it.chars)?.let { match ->
                        reportError(it.startOffset + match.range.start, linkRef.endOffset, description)
                    }
                }
            }
        }*/
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

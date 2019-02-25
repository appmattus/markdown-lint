package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule

class MD034 : Rule("NoBareUrls") {
    override val description = "Bare URL used"
    override val tags = listOf("links", "url")

    override fun visitDocument(document: MarkdownDocument) {
        document.autoLinks.forEach {
            if (it.openingMarker.isEmpty && it.closingMarker.isEmpty) {
                reportError(it.startOffset, it.endOffset, description)
            }
        }
    }
}

/*
rule "MD034", "Bare URL used" do
  tags :links, :url
  aliases 'no-bare-urls'
  check do |doc|
    doc.matching_text_element_lines(/https?:\/\//)
  end
end
 */

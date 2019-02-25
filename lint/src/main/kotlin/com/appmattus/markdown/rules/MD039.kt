package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule

class MD039 : Rule("NoSpaceInLinks") {
    override val description = "Spaces inside link text"
    override val tags = listOf("whitespace", "links")

    override fun visitDocument(document: MarkdownDocument) {
        document.links.forEach {
            if (it.textOpeningMarker.endOffset != it.text.startOffset ||
                it.textClosingMarker.startOffset != it.text.endOffset
            ) {
                reportError(it.startOffset, it.endOffset, description)
            }
        }
    }
}

/*
rule "MD039", "Spaces inside link text" do
  tags :whitespace, :links
  aliases 'no-space-in-links'
  check do |doc|
    doc.element_linenumbers(
      doc.find_type_elements(:a).select{|e|
      e.children.first.type == :text && e.children.last.type == :text and (
        e.children.first.value.start_with?(" ") or
        e.children.last.value.end_with?(" "))}
    )
  end
end
 */

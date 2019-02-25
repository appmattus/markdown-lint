package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.vladsch.flexmark.ast.ListItem

class MD026(private val punctuation: String = ".,;:!?") : Rule("NoTrailingPunctuation") {
    override val description = "Trailing punctuation in header"
    override val tags = listOf("headers")

    override fun visitDocument(document: MarkdownDocument) {
        document.headings.filterNot { it.parent is ListItem }.filter { heading ->
            punctuation.contains(heading.text.lastChar())
        }.forEach { heading ->
            reportError(heading.startOffset, heading.endOffset, description)
        }
    }
}

/*
rule "MD026", "Trailing punctuation in header" do
  tags :headers
  aliases 'no-trailing-punctuation'
  params :punctuation => '.,;:!?'
  check do |doc|
    doc.find_type(:header).select {
      |h| h[:raw_text].match(/[#{params[:punctuation]}]$/) }.map {
      |h| doc.element_linenumber(h) }
  end
end
 */

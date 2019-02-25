package com.appmattus.markdown.rules;

import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule

class MD004(val style: UnorderedListStyle = UnorderedListStyle.Consistent) : Rule("UlStyle") {
    override val description = "Unordered list style"
    override val tags = listOf("bullet", "ul")

    override fun visitDocument(document: MarkdownDocument) {

        val bullets = document.unorderedListItems

        if (bullets.isEmpty()) {
            return
        }

        val docStyle = if (style == UnorderedListStyle.Consistent) bullets.first().style() else style

        bullets.forEach {
            if (it.style() != docStyle) {
                reportError(it.startOffset, it.endOffset, description)
            }
        }
    }
}

/*
rule "MD004", "Unordered list style" do
  tags :bullet, :ul
  aliases 'ul-style'
  # :style can be one of :consistent, :asterisk, :plus, :dash
  params :style => :consistent
  check do |doc|
    bullets = doc.find_type_elements(:ul).map {|l|
      doc.find_type_elements(:li, false, l.children)}.flatten
    if bullets.empty?
      nil
    else
      if @params[:style] == :consistent
        doc_style = doc.list_style(bullets.first)
      else
        doc_style = @params[:style]
      end
      bullets.map { |b| doc.element_linenumber(b) \
                    if doc.list_style(b) != doc_style }.compact
    end
  end
end
 */

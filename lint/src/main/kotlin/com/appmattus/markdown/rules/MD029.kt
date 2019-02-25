package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.vladsch.flexmark.ast.OrderedList

class MD029(val style: OrderedListStyle = OrderedListStyle.One) : Rule("OlPrefix") {
    override val description = "Ordered list item prefix"
    override val tags = listOf("ol")

    override fun visitDocument(document: MarkdownDocument) {
        when (style) {
            OrderedListStyle.One -> {
                document.orderedListItems.forEach {
                    if (it.openingMarker.toString() != "1.") {
                        reportError(it.openingMarker.startOffset, it.openingMarker.endOffset, description)
                    }
                }
            }
            OrderedListStyle.Ordered -> {
                document.orderedListItems.forEach {
                    val startIndex = (it.parent as OrderedList).startNumber

                    if (it.openingMarker.toString() != "${startIndex + it.index()}.") {
                        reportError(it.openingMarker.startOffset, it.openingMarker.endOffset, description)
                    }
                }
            }
        }
    }
}

/*
rule "MD029", "Ordered list item prefix" do
  tags :ol
  aliases 'ol-prefix'
  # Style can be :one or :ordered
  params :style => :one
  check do |doc|
    if params[:style] == :ordered
      doc.find_type_elements(:ol).map { |l|
        doc.find_type_elements(:li, false, l.children).map.with_index { |i, idx|
          doc.element_linenumber(i) \
            unless doc.element_line(i).strip.start_with?("#{idx+1}. ")
        }
      }.flatten.compact
    elsif params[:style] == :one
      doc.find_type_elements(:ol).map { |l|
        doc.find_type_elements(:li, false, l.children) }.flatten.map { |i|
          doc.element_linenumber(i) \
            unless doc.element_line(i).strip.start_with?('1. ') }.compact
    end
  end
end
 */

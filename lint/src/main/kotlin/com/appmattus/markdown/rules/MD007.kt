package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule

class MD007(private val indent: Int = 2) : Rule("UlStartLeft") {
    override val description = "Consider starting bulleted lists at the beginning of the line"
    override val tags = listOf("bullet", "ul", "indentation")

    override fun visitDocument(document: MarkdownDocument) {
        document.unorderedListItems.forEach {
            if (it.indent() != it.level() * indent) {
                reportError(it.startOffset, it.endOffset, description)
            }
        }
    }
}

/*
rule "MD007", "Unordered list indentation" do
  tags :bullet, :ul, :indentation
  aliases 'ul-indent'
  params :indent => 2
  check do |doc|
    indents = []
    errors = []
    indents = doc.find_type(:ul).map {
      |e| [doc.indent_for(doc.element_line(e)), doc.element_linenumber(e)] }
    curr_indent = indents[0][0] unless indents.empty?
    indents.each do |indent, linenum|
      if indent > curr_indent and indent - curr_indent != @params[:indent]
        errors << linenum
      end
      curr_indent = indent
    end
    errors
  end
end
 */

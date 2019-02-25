package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule

class MD014 : Rule("CommandsShowOutput") {
    override val description = "Dollar signs used before commands without showing output"
    override val tags = listOf("code")

    override fun visitDocument(document: MarkdownDocument) {
        document.codeBlocks.forEach { block ->
            val lines = block.contentLines.filter { it.isNotBlank() }

            if (lines.isNotEmpty() && lines.all { it.startsWith("\$ ") }) {
                reportError(block.startOffset, block.endOffset, description)
            }
        }
    }
}

/*
rule "MD014", "Dollar signs used before commands without showing output" do
  tags :code
  aliases 'commands-show-output'
  check do |doc|
    doc.find_type_elements(:codeblock).select{
      |e| not e.value.empty? and
      not e.value.split(/\n+/).map{|l| l.match(/^\$\s/)}.include?(nil)
    }.map{|e| doc.element_linenumber(e)}
  end
end
 */

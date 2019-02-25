package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule

class MD002(private val level: Int = 1) : Rule("FirstHeaderH1") {
    override val description = "First header should be a top level header"
    override val tags = listOf("headers")

    override fun visitDocument(document: MarkdownDocument) {
        document.headings.firstOrNull()?.let {
            if (it.level != level) {
                reportError(it.startOffset, it.endOffset, description)
            }
        }
    }
}

/*
rule "MD002", "First header should be a top level header" do
  tags :headers
  aliases 'first-header-h1'
  params :level => 1
  check do |doc|
    first_header = doc.find_type(:header).first
    [first_header[:location]] if first_header and first_header[:level] != @params[:level]
  end
end
 */

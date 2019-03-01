package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup

class MD041(
    private val level: Int = 1,
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule("FirstLineH1") {

    override val description = "First line in file should be a top level header"
    override val tags = listOf("headers")

    override fun visitDocument(document: MarkdownDocument) {
        document.headings.firstOrNull().let { header ->
            if (header == null || header.lineNumber != 0 || header.level != level) {
                reportError(document.lines[0].startOffset, document.lines[0].endOffset, description)
            }
        }
    }
}

/*
rule "MD041", "First line in file should be a top level header" do
  tags :headers
  aliases 'first-line-h1'
  params :level => 1
  check do |doc|
    first_header = doc.find_type(:header).first
    [1] if first_header.nil? or first_header[:location] != 1 \
      or first_header[:level] != params[:level]
  end
end
 */

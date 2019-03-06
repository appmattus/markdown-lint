package com.appmattus.markdown.rules

import com.appmattus.markdown.ErrorReporter
import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup
import com.vladsch.flexmark.ast.ListItem
import com.vladsch.flexmark.util.ast.Document

class SingleH1Rule(
    private val level: Int = 1,
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule("SingleH1") {

    override val description = "Multiple top level headers in the same document"
    override val tags = listOf("headers")

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        val headers = document.headings.filterNot { it.parent is ListItem }.filter { it.level == level }

        if (headers.isNotEmpty() && headers[0].previous == null && headers[0].parent is Document) {
            headers.drop(1).forEach { heading ->
                errorReporter.reportError(heading.startOffset, heading.endOffset, description)
            }
        }
    }
}

/*
rule "MD025", "Multiple top level headers in the same document" do
  tags :headers
  aliases 'single-h1'
  params :level => 1
  check do |doc|
    headers = doc.find_type(:header, false).select { |h| h[:level] == params[:level] }
    if not headers.empty? and doc.element_linenumber(headers[0]) == 1
      headers[1..-1].map { |h| doc.element_linenumber(h) }
    end
  end
end
 */

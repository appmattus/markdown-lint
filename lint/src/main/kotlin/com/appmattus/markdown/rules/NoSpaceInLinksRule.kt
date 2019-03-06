package com.appmattus.markdown.rules

import com.appmattus.markdown.ErrorReporter
import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup

class NoSpaceInLinksRule(override val config: RuleSetup.Builder.() -> Unit = {}) : Rule("NoSpaceInLinks") {

    override val description = "Spaces inside link text"
    override val tags = listOf("whitespace", "links")

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        document.links.forEach {
            if (it.textOpeningMarker.endOffset != it.text.startOffset ||
                it.textClosingMarker.startOffset != it.text.endOffset
            ) {
                errorReporter.reportError(it.startOffset, it.endOffset, description)
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

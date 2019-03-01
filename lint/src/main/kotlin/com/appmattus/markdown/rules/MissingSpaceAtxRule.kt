package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup
import com.appmattus.markdown.rules.config.HeaderStyle
import com.appmattus.markdown.rules.extentions.style

class MissingSpaceAtxRule(override val config: RuleSetup.Builder.() -> Unit = {}) : Rule("MissingSpaceAtx") {

    override val description = "No space after hash on atx style header"
    override val tags = listOf("headers", "atx", "spaces")

    override fun visitDocument(document: MarkdownDocument) {
        document.headings.forEach { heading ->
            if (heading.style() == HeaderStyle.Atx) {
                if (heading.openingMarker.endOffset == heading.text.startOffset) {
                    reportError(heading.startOffset, heading.endOffset, description)
                }
            }
        }
    }
}

/*
rule "MD018", "No space after hash on atx style header" do
  tags :headers, :atx, :spaces
  aliases 'no-missing-space-atx'
  check do |doc|
    doc.find_type_elements(:header).select do |h|
      doc.header_style(h) == :atx and doc.element_line(h).match(/^#+[^#\s]/)
    end.map { |h| doc.element_linenumber(h) }
  end
end
 */

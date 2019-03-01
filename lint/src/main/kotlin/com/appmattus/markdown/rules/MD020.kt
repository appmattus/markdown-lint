package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup
import com.appmattus.markdown.rules.config.HeaderStyle
import com.appmattus.markdown.rules.extentions.style

class MD020(override val config: RuleSetup.Builder.() -> Unit = {}) : Rule("NoMissingSpaceClosedAtx") {

    override val description = "No space inside hashes on closed atx style header"
    override val tags = listOf("headers", "atx_closed", "spaces")

    override fun visitDocument(document: MarkdownDocument) {
        document.headings.forEach { heading ->
            if (heading.style() == HeaderStyle.AtxClosed) {
                if (heading.openingMarker.endOffset == heading.text.startOffset ||
                    (heading.text.endOffset == heading.closingMarker.startOffset && !heading.text.endsWith("\\"))
                ) {
                    reportError(heading.startOffset, heading.endOffset, description)
                }
            }
        }
    }
}

/*
rule "MD020", "No space inside hashes on closed atx style header" do
  tags :headers, :atx_closed, :spaces
  aliases 'no-missing-space-closed-atx'
  check do |doc|
    doc.find_type_elements(:header).select do |h|
      doc.header_style(h) == :atx_closed \
        and (doc.element_line(h).match(/^#+[^#\s]/) \
             or doc.element_line(h).match(/[^#\s\\]#+$/))
    end.map { |h| doc.element_linenumber(h) }
  end
end
 */

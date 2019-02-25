package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule

class MD020 : Rule("NoMissingSpaceClosedAtx") {
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

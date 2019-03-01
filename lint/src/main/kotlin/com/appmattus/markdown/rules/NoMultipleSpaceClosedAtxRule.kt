package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup
import com.appmattus.markdown.rules.config.HeaderStyle
import com.appmattus.markdown.rules.extentions.style

class NoMultipleSpaceClosedAtxRule(override val config: RuleSetup.Builder.() -> Unit = {}) : Rule("NoMultipleSpaceClosedAtx") {

    override val description = "Multiple spaces inside hashes on closed atx style header"
    override val tags = listOf("headers", "atx_closed", "spaces")

    override fun visitDocument(document: MarkdownDocument) {
        document.headings.forEach { heading ->
            if (heading.style() == HeaderStyle.AtxClosed) {
                if ((heading.text.startOffset - heading.openingMarker.endOffset > 1) ||
                    (heading.closingMarker.startOffset - heading.text.endOffset > 1)
                ) {
                    reportError(heading.startOffset, heading.endOffset, description)
                }
            }
        }
    }
}

/*
rule "MD021", "Multiple spaces inside hashes on closed atx style header" do
  tags :headers, :atx_closed, :spaces
  aliases 'no-multiple-space-closed-atx'
  check do |doc|
    doc.find_type_elements(:header).select do |h|
      doc.header_style(h) == :atx_closed \
        and (doc.element_line(h).match(/^#+\s\s/) \
             or doc.element_line(h).match(/\s\s#+$/))
    end.map { |h| doc.element_linenumber(h) }
  end
end
 */

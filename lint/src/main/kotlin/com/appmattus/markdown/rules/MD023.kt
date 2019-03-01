package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup
import com.vladsch.flexmark.ast.ListItem

class MD023(override val config: RuleSetup.Builder.() -> Unit = {}) : Rule("HeaderStartLeft") {

    override val description = "Headers must start at the beginning of the line"
    override val tags = listOf("headers", "spaces")

    override fun visitDocument(document: MarkdownDocument) {
        document.headings.filterNot { it.parent is ListItem }.forEach { heading ->

            if (heading.indent() > 0) {
                reportError(heading.startOffset, heading.endOffset, description)
            }
        }
    }
}

/*
rule "MD023", "Headers must start at the beginning of the line" do
  tags :headers, :spaces
  aliases 'header-start-left'
  check do |doc|
    errors = []
    # The only type of header with spaces actually parsed as such is setext
    # style where only the text is indented. We check for that first.
    doc.find_type_elements(:header, false).each do |h|
      errors << doc.element_linenumber(h) if doc.element_line(h).match(/^\s/)
    end
    # Next we have to look for things that aren't parsed as headers because
    # they start with spaces.
    doc.find_type_elements(:p, false).each do |p|
      linenum = doc.element_linenumber(p)
      lines = doc.extract_text(p)
      prev_line = ""
      lines.each do |line|
        # First look for ATX style headers
        if line.match(/^\s+\#{1,6}/)
          errors << linenum
        end
        # Next, look for setext style
        if line.match(/^\s+(-+|=+)\s*$/) and not prev_line.empty?
          errors << linenum - 1
        end
        linenum += 1
        prev_line = line
      end
    end
    errors.sort
  end
end
 */

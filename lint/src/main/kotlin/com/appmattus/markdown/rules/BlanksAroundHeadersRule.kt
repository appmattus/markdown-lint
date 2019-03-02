package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup
import com.vladsch.flexmark.ast.ListItem

class BlanksAroundHeadersRule(override val config: RuleSetup.Builder.() -> Unit = {}) : Rule("BlanksAroundHeaders") {

    override val description = "Headers should be surrounded by blank lines"
    override val tags = listOf("headers", "blank_lines")

    private val whitespaceRegex = Regex("\\s*")

    override fun visitDocument(document: MarkdownDocument) {
        document.headings.filterNot { it.parent is ListItem }.forEach { heading ->

            var isValid = true
            if (heading.startLineNumber > 0) {
                if (!document.lines[heading.startLineNumber - 1].matches(whitespaceRegex)) {
                    isValid = false
                }
            }

            //heading.startLineNumber
            if (heading.endLineNumber < document.lines.size - 2) {
                if (!document.lines[heading.endLineNumber + 1].matches(whitespaceRegex)) {
                    isValid = false
                }
            }

            if (!isValid) {
                reportError(heading.startOffset, heading.endOffset, description)
            }
        }
    }
}

/*
rule "MD022", "Headers should be surrounded by blank lines" do
  tags :headers, :blank_lines
  aliases 'blanks-around-headers'
  check do |doc|
    errors = []
    doc.find_type_elements(:header, false).each do |h|
      header_bad = false
      linenum = doc.element_linenumber(h)
      # Check previous line
      if linenum > 1 and not doc.lines[linenum - 2].empty?
        header_bad = true
      end
      # Check next line
      next_line_idx = doc.header_style(h) == :setext ? linenum + 1 : linenum
      next_line = doc.lines[next_line_idx]
      header_bad = true if not next_line.nil? and not next_line.empty?
      errors << linenum if header_bad
    end
    # Kramdown requires that headers start on a block boundary, so in most
    # cases it won't pick up a header without a blank line before it. We need
    # to check regular text and pick out headers ourselves too
    doc.find_type_elements(:p, false).each do |p|
      linenum = doc.element_linenumber(p)
      text = p.children.select { |e| e.type == :text }.map {|e| e.value }.join
      lines = text.split("\n")
      prev_lines = ["", ""]
      lines.each do |line|
        # First look for ATX style headers without blank lines before
        if line.match(/^\#{1,6}/) and not prev_lines[1].empty?
          errors << linenum
        end
        # Next, look for setext style
        if line.match(/^(-+|=+)\s*$/) and not prev_lines[0].empty?
          errors << linenum - 1
        end
        linenum += 1
        prev_lines << line
        prev_lines.shift
      end
    end
    errors.sort
  end
end
 */

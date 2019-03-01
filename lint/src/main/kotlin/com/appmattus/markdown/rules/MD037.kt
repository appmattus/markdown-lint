package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup

class MD037(override val config: RuleSetup.Builder.() -> Unit = {}) : Rule("NoSpaceInEmphasis") {

    override val description = "Spaces inside emphasis markers"
    override val tags = listOf("whitespace", "emphasis")

    private val startRegex = Regex("\\s(\\*\\*?|__?)\\s.+\\1")
    private val endRegex = Regex("(\\*\\*?|__?).+\\s\\1\\s")

    override fun visitDocument(document: MarkdownDocument) {

        document.allText.forEach {
            if (it.chars.contains(startRegex) || it.chars.contains(endRegex)) {
                reportError(it.startOffset, it.endOffset, description)
            }
        }
    }
}

/*
rule "MD037", "Spaces inside emphasis markers" do
  tags :whitespace, :emphasis
  aliases 'no-space-in-emphasis'
  check do |doc|
    # Kramdown doesn't parse emphasis with spaces, which means we can just
    # look for emphasis patterns inside regular text with spaces just inside
    # them.
    (doc.matching_text_element_lines(/\s(\*\*?|__?)\s.+\1/) | \
      doc.matching_text_element_lines(/(\*\*?|__?).+\s\1\s/)).sort
  end
end
 */

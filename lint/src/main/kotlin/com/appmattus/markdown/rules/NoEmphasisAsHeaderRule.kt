package com.appmattus.markdown.rules

import com.appmattus.markdown.ErrorReporter
import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup
import com.vladsch.flexmark.ast.Emphasis
import com.vladsch.flexmark.ast.StrongEmphasis
import com.vladsch.flexmark.ast.Text

class NoEmphasisAsHeaderRule(
    private val punctuation: String = ".,;:!?",
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule("NoEmphasisAsHeader") {

    override val description = "Emphasis used instead of a header"
    override val tags = listOf("headers", "emphasis")

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {

        document.topLevelParagraphs.filter { it.lineCount == 1 }.filter { it.firstChild == it.lastChild }.forEach {
            val wrapper = it.firstChild
            if (wrapper is Emphasis || wrapper is StrongEmphasis) {
                // Ensure there is only a single Text element
                if (wrapper.firstChild == wrapper.lastChild && wrapper.firstChild is Text) {
                    // Check sentence doesn't end in punctuation
                    if (!punctuation.contains(wrapper.firstChild.chars.lastChar())) {
                        errorReporter.reportError(wrapper.startOffset, wrapper.endOffset, description)
                    }
                }
            }
        }
    }
}

/*
rule "MD036", "Emphasis used instead of a header" do
  tags :headers, :emphasis
  aliases 'no-emphasis-as-header'
  params :punctuation => '.,;:!?'
  check do |doc|
    # We are looking for a paragraph consisting entirely of emphasized
    # (italic/bold) text.
    errors = []
    doc.find_type_elements(:p, false).each do |p|
      next if p.children.length > 1
      next unless [:em, :strong].include?(p.children[0].type)
      lines = doc.extract_text(p.children[0], "", false)
      next if lines.length > 1
      next if lines.empty?
      next if lines[0].match(/[#{params[:punctuation]}]$/)
      errors << doc.element_linenumber(p)
    end
    errors
  end
end
 */

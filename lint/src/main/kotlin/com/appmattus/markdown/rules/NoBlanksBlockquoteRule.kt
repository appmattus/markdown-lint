package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup
import com.vladsch.flexmark.ast.BlockQuote

class NoBlanksBlockquoteRule(override val config: RuleSetup.Builder.() -> Unit = {}) : Rule("NoBlanksBlockquote") {

    override val description = "Blank line inside blockquote"
    override val tags = listOf("blockquote", "whitespace")

    override fun visitDocument(document: MarkdownDocument) {
        document.blockQuotes.filter { it.next is BlockQuote }.forEach {
            reportError(it.startOffset, it.next.startOffset, description)
        }
    }
}

/*
rule "MD028", "Blank line inside blockquote" do
  tags :blockquote, :whitespace
  aliases 'no-blanks-blockquote'
  check do |doc|
    def check_blockquote(errors, elements)
      prev = [nil, nil, nil]
      elements.each do |e|
        prev.shift
        prev << e.type
        if prev == [:blockquote, :blank, :blockquote]
          # The current location is the start of the second blockquote, so the
          # line before will be a blank line in between the two, or at least the
          # lowest blank line if there are more than one.
          errors << e.options[:location] - 1
        end
        check_blockquote(errors, e.children)
      end
    end
    errors = []
    check_blockquote(errors, doc.elements)
    errors
  end
end
 */

package com.appmattus.markdown.rules

import com.appmattus.markdown.ErrorReporter
import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup
import com.vladsch.flexmark.ast.Paragraph

class NoMultipleSpaceBlockquoteRule(override val config: RuleSetup.Builder.() -> Unit = {}) : Rule("NoMultipleSpaceBlockquote") {

    override val description = "Multiple spaces after blockquote symbol"
    override val tags = listOf("blockquote", "whitespace", "indentation")

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        document.blockQuotes.map { it.firstChild as Paragraph }.forEach { paragraph ->
            paragraph.lineIndents.forEachIndexed { index, i ->
                if (i > 0) {
                    val item = paragraph.contentLines[index]
                    errorReporter.reportError(item.startOffset, item.endOffset, description)
                }
            }
        }
    }
}

/*
rule "MD027", "Multiple spaces after blockquote symbol" do
  tags :blockquote, :whitespace, :indentation
  aliases 'no-multiple-space-blockquote'
  check do |doc|
    errors = []
    doc.find_type_elements(:blockquote).each do |e|
      linenum = doc.element_linenumber(e)
      lines = doc.extract_text(e, /^\s*> /)
      lines.each do |line|
        errors << linenum if line.start_with?(" ")
        linenum += 1
      end
    end
    errors
  end
end
 */

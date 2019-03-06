package com.appmattus.markdown.rules

import com.appmattus.markdown.ErrorReporter
import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup
import com.appmattus.markdown.rules.extentions.indent

class UlStartLeftRule(override val config: RuleSetup.Builder.() -> Unit = {}) : Rule("UlStartLeft") {

    override val description = "Consider starting bulleted lists at the beginning of the line"
    override val tags = listOf("bullet", "ul", "indentation")

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        document.topLevelListBlocks.forEach {
            if (it.indent() != 0) {
                errorReporter.reportError(it.startOffset, it.endOffset, description)
            }
        }
    }
}

/*
rule "", "Consider starting bulleted lists at the beginning of the line" do
  # Starting at the beginning of the line means that indendation for each
  # bullet level can be identical.
  tags :bullet, :ul, :indentation
  aliases 'ul-start-left'
  check do |doc|
    doc.find_type(:ul, false).select{
      |e| doc.indent_for(doc.element_line(e)) != 0 }.map{ |e| e[:location] }
  end
end
 */

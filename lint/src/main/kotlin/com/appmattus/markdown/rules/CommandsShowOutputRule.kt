package com.appmattus.markdown.rules

import com.appmattus.markdown.ErrorReporter
import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup

class CommandsShowOutputRule(override val config: RuleSetup.Builder.() -> Unit = {}) : Rule("CommandsShowOutput") {

    override val description = "Dollar signs used before commands without showing output"
    override val tags = listOf("code")

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        document.codeBlocks.forEach { block ->
            val lines = block.contentLines.filter { it.isNotBlank() }

            if (lines.isNotEmpty() && lines.all { it.startsWith("\$ ") }) {
                errorReporter.reportError(block.startOffset, block.endOffset, description)
            }
        }
    }
}

/*
rule "MD014", "Dollar signs used before commands without showing output" do
  tags :code
  aliases 'commands-show-output'
  check do |doc|
    doc.find_type_elements(:codeblock).select{
      |e| not e.value.empty? and
      not e.value.split(/\n+/).map{|l| l.match(/^\$\s/)}.include?(nil)
    }.map{|e| doc.element_linenumber(e)}
  end
end
 */

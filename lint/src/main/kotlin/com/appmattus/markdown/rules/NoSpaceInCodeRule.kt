package com.appmattus.markdown.rules

import com.appmattus.markdown.ErrorReporter
import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup
import com.appmattus.markdown.rules.extentions.canTrim

class NoSpaceInCodeRule(override val config: RuleSetup.Builder.() -> Unit = {}) : Rule("NoSpaceInCode") {

    override val description = "Spaces inside code span elements"
    override val tags = listOf("whitespace", "code")

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        document.inlineCode.forEach {
            if (it.text.canTrim()) {
                errorReporter.reportError(it.startOffset, it.endOffset, description)
            }
        }
    }
}

/*
rule "MD038", "Spaces inside code span elements" do
  tags :whitespace, :code
  aliases 'no-space-in-code'
  check do |doc|
    # We only want to check single line codespan elements and not fenced code
    # block that happen to be parsed as code spans.
    doc.element_linenumbers(doc.find_type_elements(:codespan).select{
      |i| i.value.match(/(^\s|\s$)/) and not i.value.include?("\n")})
  end
end
 */

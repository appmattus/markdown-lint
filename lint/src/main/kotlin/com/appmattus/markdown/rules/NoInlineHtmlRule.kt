package com.appmattus.markdown.rules

import com.appmattus.markdown.ErrorReporter
import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup

class NoInlineHtmlRule(override val config: RuleSetup.Builder.() -> Unit = {}) : Rule("NoInlineHtml") {

    override val description = "Inline HTML"
    override val tags = listOf("html")

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        document.htmlElements.forEach {
            errorReporter.reportError(it.startOffset, it.endOffset, description)
        }
    }
}

/*
rule "MD033", "Inline HTML" do
  tags :html
  aliases 'no-inline-html'
  check do |doc|
    doc.element_linenumbers(doc.find_type(:html_element))
  end
end
 */

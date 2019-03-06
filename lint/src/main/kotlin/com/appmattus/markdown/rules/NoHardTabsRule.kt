package com.appmattus.markdown.rules

import com.appmattus.markdown.ErrorReporter
import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup

class NoHardTabsRule(override val config: RuleSetup.Builder.() -> Unit = {}) : Rule("NoHardTabs") {
    override val description = "Hard tabs"
    override val tags = listOf("whitespace", "hard_tab")

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        document.chars.indexOfAll("\t").forEach {
            errorReporter.reportError(it, it + 1, description)
        }
    }
}

/*
rule "MD010", "Hard tabs" do
  tags :whitespace, :hard_tab
  aliases 'no-hard-tabs'
  check do |doc|
    doc.matching_lines(/\t/)
  end
end
 */

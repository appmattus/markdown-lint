package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.vladsch.flexmark.util.ast.Document

class MD010 : Rule("NoHardTabs") {
    override val description = "Hard tabs"
    override val tags = listOf("whitespace", "hard_tab")

    override fun visitDocument(document: MarkdownDocument) {
        document.chars.indexOfAll("\t").forEach {
            reportError(it, it + 1, description)
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

package com.appmattus.markdown.rules

import com.appmattus.markdown.ErrorReporter
import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup

class HeaderIncrementRule(override val config: RuleSetup.Builder.() -> Unit = {}) : Rule("HeaderIncrementRule") {

    override val description = "Header levels should only increment by one level at a time"
    override val tags = listOf("headers")

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        var oldLevel: Int? = null

        document.headings.forEach { heading ->
            if (oldLevel?.let { heading.level > it + 1 } == true) {
                errorReporter.reportError(heading.startOffset, heading.endOffset, description)
            }
            oldLevel = heading.level
        }
    }
}

/*
rule "MD001", "Header levels should only increment by one level at a time" do
  tags :headers
  aliases 'header-increment'
  check do |doc|
    headers = doc.find_type(:header)
    old_level = nil
    errors = []
    headers.each do |h|
      if old_level and h[:level] > old_level + 1
        errors << h[:location]
      end
      old_level = h[:level]
    end
    errors
  end
end
 */

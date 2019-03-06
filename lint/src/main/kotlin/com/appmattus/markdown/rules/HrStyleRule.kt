package com.appmattus.markdown.rules

import com.appmattus.markdown.ErrorReporter
import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup
import com.appmattus.markdown.rules.config.HorizontalRuleStyle

class HrStyleRule(
    val style: HorizontalRuleStyle = HorizontalRuleStyle.Consistent,
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule("HrStyle") {

    override val description = "Horizontal rule style"
    override val tags = listOf("hr")

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        val hr = document.horizontalRules

        if (hr.isNotEmpty()) {
            val expectedChars = when (style) {
                is HorizontalRuleStyle.Consistent -> hr[0].chars.toString()
                is HorizontalRuleStyle.Exact -> style.chars
            }

            hr.forEach {
                if (it.chars.toString() != expectedChars) {
                    errorReporter.reportError(it.startOffset, it.endOffset, description)
                }
            }
        }
    }
}

/*
rule "MD035", "Horizontal rule style" do
  tags :hr
  aliases 'hr-style'
  params :style => :consistent
  check do |doc|
    hrs = doc.find_type(:hr)
    if hrs.empty?
      []
    else
      if params[:style] == :consistent
        doc_style = doc.element_line(hrs[0])
      else
        doc_style = params[:style]
      end
      doc.element_linenumbers(hrs.select{|e| doc.element_line(e) != doc_style})
    end
  end
end
 */

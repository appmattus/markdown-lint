package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup
import com.appmattus.markdown.rules.config.HeaderStyle
import com.appmattus.markdown.rules.extentions.style

class ConsistentHeaderStyleRule(
    val style: HeaderStyle = HeaderStyle.Consistent,
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule("ConsistentHeaderStyle") {

    override val description = "Header style"
    override val tags = listOf("headers")

    override fun visitDocument(document: MarkdownDocument) {

        val headings = document.headings
        if (headings.isEmpty()) {
            return
        }

        val docStyle = if (style == HeaderStyle.Consistent) headings.first().style() else style

        headings.forEach {
            if (docStyle == HeaderStyle.SetextWithAtx) {
                if (it.style() != HeaderStyle.Setext && !(it.style() == HeaderStyle.Atx && it.level > 2)) {
                    reportError(it.startOffset, it.endOffset, description)
                }
            } else {
                if (it.style() != docStyle) {
                    reportError(it.startOffset, it.endOffset, description)
                }
            }
        }
    }
}

/*
rule "MD003", "Header style" do
  # Header styles are things like ### and adding underscores
  # See http://daringfireball.net/projects/markdown/syntax#header
  tags :headers
  aliases 'header-style'
  # :style can be one of :consistent, :atx, :atx_closed, :setext
  params :style => :consistent
  check do |doc|
    headers = doc.find_type_elements(:header, false)
    if headers.empty?
      nil
    else
      if @params[:style] == :consistent
        doc_style = doc.header_style(headers.first)
      else
        doc_style = @params[:style]
      end
      if doc_style == :setext_with_atx
        headers.map { |h| doc.element_linenumber(h) \
                      unless doc.header_style(h) == :setext or \
                        (doc.header_style(h) == :atx and \
                         h.options[:level] > 2) }.compact
      else
        headers.map { |h| doc.element_linenumber(h) \
                      if doc.header_style(h) != doc_style }.compact
      end
    end
  end
end
 */

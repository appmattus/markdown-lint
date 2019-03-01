package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup
import com.vladsch.flexmark.util.sequence.BasedSequence

class MD032(override val config: RuleSetup.Builder.() -> Unit = {}) : Rule("BlanksAroundLists") {

    override val description = "Lists should be surrounded by blank lines"
    override val tags = listOf("bullet", "ul", "ol", "blank_lines")

    private val fenceRegEx = Regex("^(`{3,}|~{3,})")
    private val listRegEx = Regex("^([\\*\\+\\-]|(\\d+\\.))\\s")
    private val emptyRegEx = Regex("^(\\s|$)")

    override fun visitDocument(document: MarkdownDocument) {

        var inList = false
        var inCode = false
        var fence: String? = null
        var prevLine: BasedSequence = BasedSequence.NULL

        val lines = document.lines

        lines.forEach { line ->
            if (!inCode) {
                val listMarker = listRegEx.find(line.trim())?.value

                if (!inList && listMarker != null && !prevLine.contains(emptyRegEx)) {
                    reportError(line.startOffset, line.endOffset, description)
                } else if (inList && listMarker == null && !line.contains(emptyRegEx)) {
                    reportError(prevLine.startOffset, prevLine.endOffset, description)
                }
                inList = listMarker != null
            }

            fenceRegEx.find(line.trim())?.let {
                if (!inCode || it.value.take(fence?.length ?: 0) == fence) {
                    fence = if (inCode) null else it.value
                    inCode = !inCode
                    inList = false
                }
            }
            prevLine = line
        }
    }
}

/*
rule "MD032", "Lists should be surrounded by blank lines" do
  tags :bullet, :ul, :ol, :blank_lines
  aliases 'blanks-around-lists'
  check do |doc|
    errors = []
    # Some parsers (including kramdown) have trouble detecting lists
    # without surrounding whitespace, so examine the lines directly.
    in_list = false
    in_code = false
    fence = nil
    prev_line = ""
    doc.lines.each_with_index do |line, linenum|
      if not in_code
        list_marker = line.strip.match(/^([\*\+\-]|(\d+\.))\s/)
        if list_marker and not in_list and not prev_line.match(/^($|\s)/)
          errors << linenum + 1
        elsif not list_marker and in_list and not line.match(/^($|\s)/)
          errors << linenum
        end
        in_list = list_marker
      end
      line.strip.match(/^(`{3,}|~{3,})/)
      if $1 and (not in_code or $1.slice(0, fence.length) == fence)
        fence = in_code ? nil : $1
        in_code = !in_code
        in_list = false
      end
      prev_line = line
    end
    errors.uniq
  end
end
 */

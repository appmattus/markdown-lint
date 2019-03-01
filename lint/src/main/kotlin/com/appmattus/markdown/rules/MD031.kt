package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup
import com.vladsch.flexmark.util.sequence.BasedSequence

class MD031(override val config: RuleSetup.Builder.() -> Unit = {}) : Rule("BlanksAroundFences") {

    override val description = "Fenced code blocks should be surrounded by blank lines"
    override val tags = listOf("code", "blank_lines")

    private val fenceRegEx = Regex("^(`{3,}|~{3,})")

    override fun visitDocument(document: MarkdownDocument) {

        var inCode = false
        var fence: String? = null

        val lines = document.lines.toMutableList().apply {
            add(document.lines.size, BasedSequence.NULL)
            add(0, BasedSequence.NULL)
        }.toList()

        lines.forEachIndexed { lineNum, line ->
            fenceRegEx.find(line.trim())?.let {
                if (!inCode || it.value.take(fence?.length ?: 0) == fence) {
                    fence = if (inCode) null else it.value
                    inCode = !inCode

                    if (surroundingLinesEmpty(inCode, lines, lineNum)) {
                        reportError(line.startOffset, line.endOffset, description)
                    }
                }
            }
        }
    }

    private fun surroundingLinesEmpty(inCode: Boolean, lines: List<BasedSequence>, lineNum: Int) =
        inCode && lines[lineNum - 1].isNotEmpty() || (!inCode && lines[lineNum + 1].isNotEmpty())
}

/*
rule "MD031", "Fenced code blocks should be surrounded by blank lines" do
  tags :code, :blank_lines
  aliases 'blanks-around-fences'
  check do |doc|
    errors = []
    # Some parsers (including kramdown) have trouble detecting fenced code
    # blocks without surrounding whitespace, so examine the lines directly.
    in_code = false
    fence = nil
    lines = [ "" ] + doc.lines + [ "" ]
    lines.each_with_index do |line, linenum|
      line.strip.match(/^(`{3,}|~{3,})/)
      if $1 and (not in_code or $1.slice(0, fence.length) == fence)
        fence = in_code ? nil : $1
        in_code = !in_code
        if (in_code and not lines[linenum - 1].empty?) or
           (not in_code and not lines[linenum + 1].empty?)
          errors << linenum
        end
      end
    end
    errors
  end
end
 */

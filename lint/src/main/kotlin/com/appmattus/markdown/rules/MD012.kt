package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule

class MD012 : Rule("NoMultipleBlanks") {
    override val description = "Multiple consecutive blank lines"
    override val tags = listOf("whitespace", "blank_lines")

    private val regex = Regex("^\\s*(\r?\n|\n)\\s*(\r?\n|\n|$)", RegexOption.MULTILINE)

    override fun visitDocument(document: MarkdownDocument) {
        val codeBlocks = document.codeBlocks.map { IntRange(it.startLineNumber, it.endLineNumber) }

        regex.findAll(document.chars).map { match ->
            IntRange(match.range.start, match.range.endInclusive)
        }.filter { range ->
            (codeBlocks.find { it.contains(document.getLineNumber(range.start)) } == null)
        }.forEach { range ->
            reportError(range.start, range.endInclusive, description)
        }
    }
}

/*
rule "MD012", "Multiple consecutive blank lines" do
  tags :whitespace, :blank_lines
  aliases 'no-multiple-blanks'
  check do |doc|
    # Every line in the document that is part of a code block. Blank lines
    # inside of a code block are acceptable.
    codeblock_lines = doc.find_type_elements(:codeblock).map{
      |e| (doc.element_linenumber(e)..
           doc.element_linenumber(e) + e.value.lines.count).to_a }.flatten
    blank_lines = doc.matching_lines(/^\s*$/)
    cons_blank_lines = blank_lines.each_cons(2).select{
      |p, n| n - p == 1}.map{|p, n| n}
    cons_blank_lines - codeblock_lines
  end
end
 */

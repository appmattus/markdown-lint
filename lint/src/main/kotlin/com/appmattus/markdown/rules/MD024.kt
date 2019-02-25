package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.vladsch.flexmark.ast.ListItem

class MD024(private val allowDifferentNesting: Boolean = false) : Rule("NoDuplicateHeader") {
    override val description = "Multiple headers with the same content"
    override val tags = listOf("headers")

    override fun visitDocument(document: MarkdownDocument) {
        if (!allowDifferentNesting) {
            val headingsMap = mutableListOf<String>()
            document.headings.filterNot { it.parent is ListItem }.forEach { heading ->
                if (headingsMap.contains(heading.text.toString())) {
                    reportError(heading.startOffset, heading.endOffset, description)
                } else {
                    headingsMap.add(heading.text.toString())
                }
            }
        } else {
            val stack: Array<MutableList<String>> = arrayOf(
                mutableListOf(),
                mutableListOf(),
                mutableListOf(),
                mutableListOf(),
                mutableListOf(),
                mutableListOf()
            )

            document.headings.filterNot { it.parent is ListItem }.forEach { heading ->
                val text = heading.text.toString()

                if ((0 until heading.level).any { stack[it].contains(text) }) {
                    reportError(heading.startOffset, heading.endOffset, description)
                } else {
                    stack[heading.level - 1].add(text)
                    @Suppress("MagicNumber")
                    (heading.level until 6).forEach { stack[it] = mutableListOf() }
                }
            }
        }
    }
}

/*
rule "MD024", "Multiple headers with the same content" do
  tags :headers
  aliases 'no-duplicate-header'
  params :allow_different_nesting => false
  check do |doc|
    headers = doc.find_type(:header)
    allow_different_nesting = params[:allow_different_nesting]

    duplicates = headers.select do |h|
      headers.any? do |e|
        e[:location] < h[:location] &&
          e[:raw_text] == h[:raw_text] &&
          (allow_different_nesting == false || e[:level] != h[:level])
      end
    end.to_set

    if allow_different_nesting
      same_nesting_duplicates = Set.new
      stack = []
      current_level = 0
      doc.find_type(:header).each do |header|
        level = header[:level]
        text = header[:raw_text]

        if current_level > level
          stack.pop
        elsif current_level < level
          stack.push([text])
        else
          same_nesting_duplicates.add(header) if stack.last.include?(text)
        end

        current_level = level
      end

      duplicates += same_nesting_duplicates
    end

    duplicates.map { |h| doc.element_linenumber(h) }
  end
end
 */

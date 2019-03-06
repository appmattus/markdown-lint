package com.appmattus.markdown.rules

import com.appmattus.markdown.ErrorReporter
import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup
import com.appmattus.markdown.rules.extentions.splitIntoLines

class LineLengthRule(
    val lineLength: Int = 80,
    val codeBlocks: Boolean = true,
    val tables: Boolean = true,
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule("LineLength") {

    override val description = "Line length"
    override val tags = listOf("line_length")

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        var result = document.chars.splitIntoLines().filter {
            it.length > lineLength
        }.map {
            IntRange(it.startOffset + lineLength, it.endOffset)
        }

        // Remove links at the end of the file
        val links = document.allLinks.map { IntRange(it.startOffset, it.endOffset) }
        result = result.filter { range ->
            links.find {
                it.endInclusive == range.endInclusive && document.chars.getColumnAtIndex(it.start) <= lineLength
            } == null
        }

        if (!codeBlocks) {
            val codeBlocks = document.codeBlocks.map { IntRange(it.startLineNumber, it.endLineNumber) }
            result = result.filter { range ->
                (codeBlocks.find { it.contains(document.getLineNumber(range.start)) } == null)
            }
        }

        if (!tables) {
            val tables = document.tables.map { IntRange(it.startLineNumber, it.endLineNumber) }
            result = result.filter { range ->
                (tables.find { it.contains(document.getLineNumber(range.start)) } == null)
            }
        }

        result.forEach { range ->
            errorReporter.reportError(range.start, range.endInclusive, description)
        }
    }
}

/*
rule "MD013", "Line length" do
  tags :line_length
  aliases 'line-length'
  params :line_length => 80, :code_blocks => true, :tables => true
  check do |doc|
    # Every line in the document that is part of a code block.
    codeblock_lines = doc.find_type_elements(:codeblock).map{
      |e| (doc.element_linenumber(e)..
           doc.element_linenumber(e) + e.value.lines.count).to_a }.flatten
    # Every line in the document that is part of a table.
    locations = doc.elements
                .map { |e| [e.options[:location], e] }
                .reject { |l, _| l.nil? }
    table_lines = locations.map.with_index {
      |(l, e), i| (i + 1 < locations.size ?
                   (l..locations[i+1].first - 1) :
                   (l..doc.lines.count)).to_a if e.type == :table }.flatten
    overlines = doc.matching_lines(/^.{#{@params[:line_length]}}.*\s/)
    overlines -= codeblock_lines unless params[:code_blocks]
    overlines -= table_lines unless params[:tables]
    overlines
  end
end
 */

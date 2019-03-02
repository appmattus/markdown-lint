package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup
import com.vladsch.flexmark.ast.BulletList
import com.vladsch.flexmark.ast.ListItem
import com.vladsch.flexmark.ast.OrderedList

class ListMarkerSpaceRule(
    val ulSingle: Int = 1,
    val olSingle: Int = 1,
    val ulMulti: Int = 1,
    val olMulti: Int = 1,
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule("ListMarkerSpace") {

    override val description = "Spaces after list markers"
    override val tags = listOf("ol", "ul", "whitespace")

    override fun visitDocument(document: MarkdownDocument) {

        document.listBlocks.forEach { listBlock ->
            val single = listBlock.children.all {
                it.firstChild == it.lastChild
            }

            val indent = when {
                listBlock is OrderedList && single -> olSingle
                listBlock is OrderedList && !single -> olMulti
                listBlock is BulletList && single -> ulSingle
                listBlock is BulletList && !single -> ulMulti
                else -> throw IllegalStateException()
            }

            listBlock.children.forEach {
                it as ListItem
                if (it.firstChild.startOffset - it.openingMarker.endOffset != indent) {
                    reportError(it.startOffset, it.endOffset, description)
                }
            }
        }
    }
}

/*
rule "MD030", "Spaces after list markers" do
  tags :ol, :ul, :whitespace
  aliases 'list-marker-space'
  params :ul_single => 1, :ol_single => 1, :ul_multi => 1, :ol_multi => 1
  check do |doc|
    errors = []
    doc.find_type_elements([:ul, :ol]).each do |l|
      list_type = l.type.to_s
      items = doc.find_type_elements(:li, false, l.children)
      # The entire list is to use the multi-paragraph spacing rule if any of
      # the items in it have multiple paragraphs/other block items.
      srule = items.map { |i| i.children.length }.max > 1 ? "multi" : "single"
      items.each do |i|
        actual_spaces = doc.element_line(i).match(/^\s*\S+(\s+)/)[1].length
        required_spaces = params["#{list_type}_#{srule}".to_sym]
        errors << doc.element_linenumber(i) if required_spaces != actual_spaces
      end
    end
    errors
  end
end
 */

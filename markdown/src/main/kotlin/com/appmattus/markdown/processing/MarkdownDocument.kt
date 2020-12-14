package com.appmattus.markdown.processing

import com.appmattus.markdown.rules.extensions.referenceUrl
import com.appmattus.markdown.rules.extensions.splitIntoLines
import com.vladsch.flexmark.ast.AutoLink
import com.vladsch.flexmark.ast.BlockQuote
import com.vladsch.flexmark.ast.BulletList
import com.vladsch.flexmark.ast.BulletListItem
import com.vladsch.flexmark.ast.Code
import com.vladsch.flexmark.ast.Emphasis
import com.vladsch.flexmark.ast.FencedCodeBlock
import com.vladsch.flexmark.ast.Heading
import com.vladsch.flexmark.ast.HtmlBlock
import com.vladsch.flexmark.ast.HtmlInline
import com.vladsch.flexmark.ast.Image
import com.vladsch.flexmark.ast.ImageRef
import com.vladsch.flexmark.ast.IndentedCodeBlock
import com.vladsch.flexmark.ast.Link
import com.vladsch.flexmark.ast.LinkNodeBase
import com.vladsch.flexmark.ast.LinkRef
import com.vladsch.flexmark.ast.ListBlock
import com.vladsch.flexmark.ast.ListItem
import com.vladsch.flexmark.ast.OrderedList
import com.vladsch.flexmark.ast.OrderedListItem
import com.vladsch.flexmark.ast.Paragraph
import com.vladsch.flexmark.ast.Reference
import com.vladsch.flexmark.ast.StrongEmphasis
import com.vladsch.flexmark.ast.Text
import com.vladsch.flexmark.ast.ThematicBreak
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListItem
import com.vladsch.flexmark.ext.tables.TableBlock
import com.vladsch.flexmark.util.ast.Block
import com.vladsch.flexmark.util.ast.Document
import com.vladsch.flexmark.util.ast.Node
import com.vladsch.flexmark.util.ast.NodeVisitor
import com.vladsch.flexmark.util.ast.VisitHandler
import com.vladsch.flexmark.util.sequence.BasedSequence
import java.nio.file.Path
import kotlin.reflect.KClass

class MarkdownDocument constructor(val file: Path, val document: Document) {

    val filename: String = file.fileName.toString()

    val headings: List<Heading> by lazy { document.find(Heading::class) }
    val htmlElements: List<Node> by lazy { document.find(HtmlBlock::class, HtmlInline::class) }
    val orderedListItems: List<OrderedListItem> by lazy { document.find(OrderedListItem::class) }
    val unorderedListItems: List<BulletListItem> by lazy { document.find(BulletListItem::class) }
    val taskListItems: List<TaskListItem> by lazy { document.find(TaskListItem::class) }
    val listItems: List<ListItem> by lazy { document.find(OrderedListItem::class, BulletListItem::class) }

    val listBlocks: List<ListBlock> by lazy { document.find(OrderedList::class, BulletList::class) }

    val topLevelListBlocks: List<ListBlock> by lazy {
        document.find(OrderedList::class, BulletList::class, visitChildren = false)
    }
    val linkRefs: List<LinkRef> by lazy { document.find(LinkRef::class) }
    val fencedCodeBlocks: List<FencedCodeBlock> by lazy { document.find(FencedCodeBlock::class) }
    val codeBlocks: List<Block> by lazy { document.find(FencedCodeBlock::class, IndentedCodeBlock::class) }
    val inlineCode: List<Code> by lazy { document.find(Code::class) }
    val allLinks: List<LinkNodeBase> by lazy {
        document.find(Link::class, Reference::class, AutoLink::class, LinkRef::class)
    }
    val allLinkUrls: List<BasedSequence> by lazy {
        allLinks.mapNotNull { link ->
            when (link) {
                is LinkRef -> link.referenceUrl()
                is Reference -> null
                else -> link.url
            }
        }
    }
    val allImageUrls: List<BasedSequence> by lazy {
        allImages.map { link ->
            when (link) {
                is ImageRef -> link.getReferenceNode(document).url
                else -> link.url
            }
        }
    }
    val links: List<Link> by lazy { document.find(Link::class) }
    val autoLinks: List<AutoLink> by lazy { document.find(AutoLink::class) }
    val allImages: List<LinkNodeBase> by lazy { document.find(Image::class, ImageRef::class) }
    val tables: List<TableBlock> by lazy { document.find(TableBlock::class) }
    val blockQuotes: List<BlockQuote> by lazy { document.find(BlockQuote::class) }
    val horizontalRules: List<ThematicBreak> by lazy { document.find(ThematicBreak::class) }
    val topLevelParagraphs: List<Paragraph> by lazy { document.find(Paragraph::class, visitChildren = false) }
    val allText by lazy { document.find(Text::class) }

    val allEmphasis by lazy { document.find(Emphasis::class, StrongEmphasis::class) }

    val chars: BasedSequence by lazy { document.chars }
    val lines by lazy { chars.splitIntoLines() }

    fun getLineNumber(offset: Int) = document.getLineNumber(offset)

    fun getColumnNumber(offset: Int) = chars.getColumnAtIndex(offset)

    private fun <T : Node> Document.find(vararg classes: KClass<out T>, visitChildren: Boolean = true): List<T> {
        val listItems = mutableListOf<T>()

        lateinit var visitor: NodeVisitor

        visitor = classes.map {
            VisitHandler<T>(it.java) { node ->
                listItems.add(node)
                if (visitChildren) {
                    visitor.visitChildren(node)
                }
            }
        }.let(::NodeVisitor)

        visitor.visit(this)

        return listItems.toList()
    }
}

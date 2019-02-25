package com.appmattus

import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.ParserFactory
import com.appmattus.markdown.rules.MD005
import com.appmattus.markdown.rules.MD010

val list2 = """1. A
   1. B
   2. CA	bh
2. D
"""

val listMarkdown = """
1. First ordered list item
2. Another item
  * Unordered sub-list.
1. Actual numbers don't matter, just that it's a number
  1. Ordered sub-list
4. And another item.

   You can have properly indented paragraphs within list items. Notice the blank line above, and the leading spaces
   (at least one, but we'll use three here to also align the raw Markdown).

   To have a line break without a paragraph, you will need to use two trailing spaces.
   Note that this line is separate, but within the same paragraph.
   (This is contrary to the typical GFM line break behaviour, where trailing spaces are not required.)

* Unordered list can use asterisks
- Or minuses
+ Or pluses
"""

fun main() {
    val parser = ParserFactory.parser

    val document = parser.parse(list2)

    val rules = listOf(MD005(), MD010())

    rules.forEach {
        it.visitDocument(MarkdownDocument("temp", document))
        println(it.errors)
    }
}

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


fun main() {

    val res1 = Kts.eval("val x = 3")
    val res2 = Kts.eval("x + 2")
    println(res2)


    val parser = ParserFactory.parser

    val document = parser.parse(list2)

    val rules = listOf(MD005(), MD010())

    rules.forEach {
        it.visitDocument(MarkdownDocument("temp", document))
        println(it.errors)
    }
}

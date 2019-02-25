package com.appmattus.markdown

fun createDocument(documentText: String) =
    MarkdownDocument("test.md", ParserFactory.parser.parse(documentText))

fun loadDocument(filename: String) =
    MarkdownDocument(
        filename,
        ParserFactory.parser.parse(MarkdownDocument::class.java.classLoader.getResource(filename).readText(Charsets.UTF_8))
    )

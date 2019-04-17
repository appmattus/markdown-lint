package com.appmattus.markdown

import com.appmattus.markdown.processing.MarkdownDocument
import com.appmattus.markdown.processing.ParserFactory
import java.io.File

private val eolRegex = "(\\r?\\n|\\n)".toRegex()

fun loadDocumentUnixEol(filename: String) =
    MarkdownDocument(
        File(MarkdownDocument::class.java.classLoader.getResource(filename).file),
        ParserFactory.parser.parse(
            MarkdownDocument::class.java.classLoader.getResource(
                filename
            ).readText(Charsets.UTF_8).replace(eolRegex, "\n")
        )
    )

fun loadDocumentWindowsEol(filename: String) =
    MarkdownDocument(
        File(MarkdownDocument::class.java.classLoader.getResource(filename).file),
        ParserFactory.parser.parse(
            MarkdownDocument::class.java.classLoader.getResource(
                filename
            ).readText(Charsets.UTF_8).replace(eolRegex, "\r\n")
        )
    )

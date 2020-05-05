package com.appmattus.markdown

import com.appmattus.markdown.processing.MarkdownDocument
import com.appmattus.markdown.processing.ParserFactory
import java.io.File
import java.net.URL

private val eolRegex = "(\\r?\\n|\\n)".toRegex()

private fun resourceUrl(filename: String): URL = MarkdownDocument::class.java.classLoader.getResource(filename)!!

fun loadDocumentUnixEol(filename: String) = MarkdownDocument(
    File(resourceUrl(filename).file),
    ParserFactory.parser.parse(resourceUrl(filename).readText(Charsets.UTF_8).replace(eolRegex, "\n"))
)

fun loadDocumentWindowsEol(filename: String) = MarkdownDocument(
    File(resourceUrl(filename).file),
    ParserFactory.parser.parse(resourceUrl(filename).readText(Charsets.UTF_8).replace(eolRegex, "\r\n"))
)

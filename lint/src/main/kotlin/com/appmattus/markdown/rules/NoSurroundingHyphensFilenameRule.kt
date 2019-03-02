package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup

class NoSurroundingHyphensFilenameRule(
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule("NoSurroundingHyphensFilenameRule") {

    override val description = "Filenames must not be wrapped by hyphens"
    override val tags = listOf("file_name")

    override fun visitDocument(document: MarkdownDocument) {

        val filename = document.filename.removeSuffix(".md")

        if (filename.startsWith("-") || filename.endsWith("-")) {
            reportError(0, 0, description)
        }
    }
}

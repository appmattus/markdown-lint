package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule

class FN002 : Rule("NoSpaceInName") {
    override val description = "Filenames must not contain spaces"
    override val tags = listOf("file_name")

    override fun visitDocument(document: MarkdownDocument) {
        if (document.filename.contains(" ")) {
            reportError(0, 0, description)
        }
    }
}

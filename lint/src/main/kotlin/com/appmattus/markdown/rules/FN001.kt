package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule

class FN001 : Rule("LowerCaseFilename") {
    override val description = "Filenames must be lowercase"
    override val tags = listOf("file_name")

    override fun visitDocument(document: MarkdownDocument) {
        if (document.filename.toLowerCase() != document.filename) {
            reportError(0, 0, description)
        }
    }
}

package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup

class FN001(override val config: RuleSetup.Builder.() -> Unit = {}) : Rule("LowerCaseFilename") {

    override val description = "Filenames must be lowercase"
    override val tags = listOf("file_name")

    override fun visitDocument(document: MarkdownDocument) {
        if (document.filename.toLowerCase() != document.filename) {
            reportError(0, 0, description)
        }
    }
}

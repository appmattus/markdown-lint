package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup

class NoWhitespaceFilenameRule(override val config: RuleSetup.Builder.() -> Unit = {}) : Rule("NoWhitespaceFilename") {

    override val description = "Filenames must not contain whitespace"
    override val tags = listOf("file_name")

    private val whitespace = "\\s".toRegex()

    override fun visitDocument(document: MarkdownDocument) {
        if (document.filename.contains(whitespace)) {
            reportError(0, 0, description)
        }
    }
}

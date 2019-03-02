package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup

class NoConsecutiveHyphensFilenameRule(
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule("NoConsecutiveHyphensFilenameRule") {

    override val description = "Filenames must not contain consecutive hyphens"
    override val tags = listOf("file_name")

    override fun visitDocument(document: MarkdownDocument) {

        if (document.filename.contains("--")) {
            reportError(0, 0, description)
        }
    }
}

package com.appmattus.markdown.rules

import com.appmattus.markdown.ErrorReporter
import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup

class NoSurroundingHyphensFilenameRule(
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule("NoSurroundingHyphensFilenameRule") {

    override val description = "Filenames must not be wrapped by hyphens"
    override val tags = listOf("file_name")

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {

        val filename = document.filename.replace(Regex("\\.(md|markdown)$"), "")

        if (filename.startsWith("-") || filename.endsWith("-")) {
            errorReporter.reportError(0, 0, description)
        }
    }
}

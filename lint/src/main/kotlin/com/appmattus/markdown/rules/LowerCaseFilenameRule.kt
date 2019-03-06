package com.appmattus.markdown.rules

import com.appmattus.markdown.ErrorReporter
import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup

class LowerCaseFilenameRule(
    private val exclusions: List<String> = listOf("README.md", "LICENSE.md"),
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule("LowerCaseFilename") {

    override val description = "Filenames must be all lowercase"
    override val tags = listOf("file_name")

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        if (document.filename.toLowerCase() != document.filename && !exclusions.contains(document.filename)) {
            errorReporter.reportError(0, 0, description)
        }
    }
}

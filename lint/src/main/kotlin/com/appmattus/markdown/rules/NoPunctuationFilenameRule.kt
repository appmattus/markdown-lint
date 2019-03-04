package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup

class NoPunctuationFilenameRule(
    punctuation: String = ".,;:!?_",
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule("NoPunctuationFilename") {

    override val description = "Filenames must not contain punctuation"
    override val tags = listOf("file_name")

    private val punctuationRegex = Regex("[${Regex.escape(punctuation)}]")

    override fun visitDocument(document: MarkdownDocument) {
        val filename = document.filename.replace(Regex("\\.(md|markdown)$"), "")

        if (filename.contains(punctuationRegex)) {
            reportError(0, 0, description)
        }
    }
}

package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup
import com.appmattus.markdown.rules.config.FilenameStyle

class FN003(
    style: FilenameStyle = FilenameStyle.Consistent,
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule("ConsistentFilenameSeparator") {

    override val description = "Filenames must not contain spaces"
    override val tags = listOf("file_name")

    private var chosenStyle = style

    private val separatorRegex = Regex("[-_\\s]")

    override fun visitDocument(document: MarkdownDocument) {
        if (chosenStyle == FilenameStyle.Consistent) {
            chosenStyle = when {
                document.filename.contains("-") -> FilenameStyle.Dash
                document.filename.contains("_") -> FilenameStyle.Underscore
                else -> FilenameStyle.Consistent
            }
        }

        separatorRegex.findAll(document.filename).forEach {
            if (when (chosenStyle) {
                    FilenameStyle.Dash -> it.value != "-"
                    FilenameStyle.Underscore -> it.value != "_"
                    else -> false
                }
            ) {
                reportError(0, 0, description)
            }
        }

        /*when (chosenStyle) {
            FilenameStyle.Dash ->
        }*/

        //if (document.filename.con)
        //if (document.filename.toLowerCase() != document.filename) {
        //    reportError(0, 0, description)
        //}
    }
}

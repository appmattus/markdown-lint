package com.appmattus.markdown.rules

import com.appmattus.markdown.ErrorReporter
import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup

/**
 * # Replace upper case letters with lower case
 *
 * Prefer to base the file name on the top-header level:
 * 1. replace upper case letters with lower case
 * 1. strip articles the, a, an from the start
 * 1. replace punctuation and white space characters by hyphens
 * 1. replace consecutive hyphens by a single hyphen
 * 1. strip surrounding hyphens
 *
 * Good:
 *
 *     file-name.md
 *
 * Bad, multiple consecutive hyphens:
 *
 *     file--name.md
 *
 * Bad, surrounding hyphens:
 *
 *     -file-name-.md
 *
 * Rationale: why not underscore or camel case? Hyphens are the most popular URL separator today, and markdown files
 * are most often used in contexts where:
 * 1. there are hyphen separated HTML files in the same project, possibly the same directory as the markdown files.
 * 1. filenames will be used directly on URLs. E.g.: GitHub blobs.
 *
 * Based on [File name](https://www.cirosantilli.com/markdown-style-guide/#file-name)
 */
class LowerCaseFilenameRule(
    private val exclusions: List<String> = listOf("README.md", "LICENSE.md"),
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    override val description = "Filenames must be all lowercase"
    override val tags = listOf("file_name")

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        if (document.filename.toLowerCase() != document.filename && !exclusions.contains(document.filename)) {
            errorReporter.reportError(0, 0, description)
        }
    }
}

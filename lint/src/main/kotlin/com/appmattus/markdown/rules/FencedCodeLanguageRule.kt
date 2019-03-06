package com.appmattus.markdown.rules

import com.appmattus.markdown.ErrorReporter
import com.appmattus.markdown.MarkdownDocument
import com.appmattus.markdown.Rule
import com.appmattus.markdown.RuleSetup

/**
 * # Fenced code blocks should have a language specified
 *
 * This rule is triggered when fenced code blocks are used, but a language isn't specified:
 *
 *     ```
 *     #!/bin/bash
 *     echo Hello world
 *     ```
 *
 * To fix this, add a language specifier to the code block:
 *
 *     ```bash
 *     #!/bin/bash
 *     echo Hello world
 *     ```
 *
 * Based on [MD040](https://github.com/markdownlint/markdownlint/blob/master/lib/mdl/rules.rb)
 */
class FencedCodeLanguageRule(
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    override val description = "Fenced code blocks should have a language specified"
    override val tags = listOf("code", "language")

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        document.fencedCodeBlocks.forEach {
            if (it.info.isEmpty) {
                errorReporter.reportError(it.startOffset, it.endOffset, description)
            }
        }
    }
}

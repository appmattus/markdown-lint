package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.processing.MarkdownDocument

/**
 * # Dollar signs used before commands without showing output
 *
 * This rule is triggered when there are code blocks showing shell commands to be typed, and the shell commands are
 * preceded by dollar signs ($):
 *
 *     $ ls
 *     $ cat foo
 *     $ less bar
 *
 * The dollar signs are unnecessary in the above situation, and should not be included:
 *
 *     ls
 *     cat foo
 *     less bar
 *
 * However, an exception is made when there is a need to distinguish between typed commands and command output, as in
 * the following example:
 *
 *     $ ls
 *     foo bar
 *     $ cat foo
 *     Hello world
 *     $ cat bar
 *     baz
 *
 * Rationale: it is easier to copy and paste and less noisy if the dollar signs are omitted when they are not needed.
 * See [Dollar signs in shell code](https://www.cirosantilli.com/markdown-style-guide/#dollar-signs-in-shell-code) for
 * more information.
 *
 * Based on [MD014](https://github.com/markdownlint/markdownlint/blob/master/lib/mdl/rules.rb)
 */
class CommandsShowOutputRule(
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    private val description = "Remove dollar sign prefix in code blocks when not showing output."

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
        document.codeBlocks.forEach { block ->
            val lines = block.contentLines.filter { it.isNotBlank() }

            if (lines.isNotEmpty() && lines.all { it.startsWith("\$ ") }) {
                errorReporter.reportError(block.startOffset, block.endOffset, description)
            }
        }
    }
}

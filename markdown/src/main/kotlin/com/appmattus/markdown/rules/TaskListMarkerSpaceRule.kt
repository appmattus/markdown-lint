package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.processing.MarkdownDocument

/**
 * # Spaces after list markers
 *
 * This rule checks for the number of spaces between a task list marker (e.g. '[ ]', '&#91;x]' or '&#91;X]') and the text of
 * the list item.
 *
 * The number of spaces checked for depends on the document style in use, but the default is 1 space after any task list
 * marker:
 *
 *     - [ ] Foo
 *     - [x] Bar
 *     - [ ] Baz
 *
 * To fix this, ensure the correct number of spaces are used after task list markers for your selected document style.
 */
class TaskListMarkerSpaceRule(
    private val indent: Int = 1,
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    override val description = "Spaces after task list markers"

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {

        document.taskListItems.forEach { item ->

            if (item.firstChild.startOffset - item.markerSuffix.endOffset != indent) {
                errorReporter.reportError(item.startOffset, item.endOffset, description)
            }
        }
    }
}

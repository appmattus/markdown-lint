package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.processing.MarkdownDocument
import com.appmattus.markdown.rules.config.TaskListItemMarkerStyle
import com.appmattus.markdown.rules.extentions.style

/**
 * # Task list item marker style
 *
 * This rule is triggered when the symbols used in the document for task list item markers do not match the configured
 * task list item marker style:
 *
 *     - [x]
 *     - [X]
 *
 * To fix this issue, use the configured style for task list item markers throughout the document:
 *
 *     - [x]
 *     - [x]
 *
 * Note: the configured task list item marker style can be a specific case ([TaskListItemMarkerStyle.Lowercase],
 * [TaskListItemMarkerStyle.Uppercase]), or simply require that the usage be
 * [TaskListItemMarkerStyle.Consistent] within the document.
 */
class ConsistentTaskListMarkerStyleRule(
    private val style: TaskListItemMarkerStyle = TaskListItemMarkerStyle.Lowercase,
    override val config: RuleSetup.Builder.() -> Unit = {}
) : Rule() {

    override val description = "Task list item marker style"
    override val tags = listOf("task-list", "ul")

    override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {

        val taskItems = document.taskListItems

        if (taskItems.isEmpty()) {
            return
        }

        val doneMarkers = taskItems.filter { it.isItemDoneMarker }

        val docStyle = if (style == TaskListItemMarkerStyle.Consistent) doneMarkers.first().style() else style

        doneMarkers.forEach {
            if (it.style() != docStyle) {
                errorReporter.reportError(it.startOffset, it.endOffset, description)
            }
        }
    }
}

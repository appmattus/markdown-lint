package com.appmattus.markdown.rules.extensions

import com.appmattus.markdown.rules.config.TaskListItemMarkerStyle
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListItem

fun TaskListItem.style(): TaskListItemMarkerStyle? {
    return when (this.markerSuffix.toString()) {
        "[x]" -> TaskListItemMarkerStyle.Lowercase
        "[X]" -> TaskListItemMarkerStyle.Uppercase
        else -> null
    }
}

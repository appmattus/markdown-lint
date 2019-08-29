package com.appmattus.markdown.rules.extensions

import com.vladsch.flexmark.ast.ListBlock
import com.vladsch.flexmark.ast.ListItem
import com.vladsch.flexmark.util.ast.Document
import com.vladsch.flexmark.util.ast.Node

fun ListItem.level(): Int {
    var cur = parent
    var level = 0

    while (cur !is Document) {
        if (cur is ListBlock) {
            level++
        }
        cur = cur.parent
    }
    return level - 1
}

fun ListItem.index(): Int {
    var cur: Node? = previous
    var index = 0

    while (cur != null) {
        cur = cur.previous
        index++
    }
    return index
}

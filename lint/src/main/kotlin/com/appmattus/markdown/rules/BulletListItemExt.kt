package com.appmattus.markdown.rules

import com.vladsch.flexmark.ast.BulletListItem

fun BulletListItem.style(): UnorderedListStyle = when (openingMarker.toString()) {
    "*" -> UnorderedListStyle.Asterisk
    "+" -> UnorderedListStyle.Plus
    "-" -> UnorderedListStyle.Dash
    else -> throw IllegalStateException()
}

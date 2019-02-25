package com.appmattus.markdown.rules

import com.vladsch.flexmark.ast.Heading

fun Heading.style(): HeaderStyle = when {
    isAtxHeading -> {
        if (closingMarker.contains("#")) HeaderStyle.AtxClosed else HeaderStyle.Atx
    }
    isSetextHeading -> HeaderStyle.Setext
    else -> throw IllegalStateException()
}

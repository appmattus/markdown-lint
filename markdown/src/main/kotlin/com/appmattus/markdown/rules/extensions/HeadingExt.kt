package com.appmattus.markdown.rules.extensions

import com.appmattus.markdown.rules.config.HeaderStyle
import com.vladsch.flexmark.ast.Heading

fun Heading.style(): HeaderStyle = when {
    isAtxHeading -> {
        if (closingMarker.contains("#")) HeaderStyle.AtxClosed else HeaderStyle.Atx
    }
    isSetextHeading -> HeaderStyle.Setext
    else -> throw IllegalStateException()
}

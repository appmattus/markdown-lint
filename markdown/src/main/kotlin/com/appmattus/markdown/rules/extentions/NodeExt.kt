package com.appmattus.markdown.rules.extentions

import com.vladsch.flexmark.util.ast.Node
import getLineNumberFixed

fun Node.indent(): Int = document.chars.getColumnAtIndex(startOffset)

val Node.startLineNumberFixed: Int
    get() {
        return document!!.getLineNumberFixed(chars.startOffset)
    }

val Node.endLineNumberFixed: Int
    get() {
        val endOffset = chars.endOffset
        return document!!.getLineNumberFixed(if (endOffset > 0) endOffset - 1 else endOffset)
    }

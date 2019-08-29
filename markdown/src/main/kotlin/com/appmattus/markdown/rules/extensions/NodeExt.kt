package com.appmattus.markdown.rules.extensions

import com.vladsch.flexmark.util.ast.Node

fun Node.indent(): Int = document.chars.getColumnAtIndex(startOffset)

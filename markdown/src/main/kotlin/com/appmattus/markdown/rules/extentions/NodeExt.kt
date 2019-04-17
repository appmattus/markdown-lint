package com.appmattus.markdown.rules.extentions

import com.vladsch.flexmark.util.ast.Node

fun Node.indent(): Int = document.chars.getColumnAtIndex(startOffset)

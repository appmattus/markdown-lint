package com.appmattus.markdown.rules.extensions

import com.vladsch.flexmark.ast.LinkRef

fun LinkRef.referenceUrl() = this.getReferenceNode(this.document)?.url

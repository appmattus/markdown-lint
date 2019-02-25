package com.appmattus.markdown.rules

import com.vladsch.flexmark.ast.LinkRef

fun LinkRef.referenceUrl() = this.getReferenceNode(this.document)?.url

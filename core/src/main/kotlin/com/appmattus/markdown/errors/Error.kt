package com.appmattus.markdown.errors

import com.appmattus.markdown.rules.Rule

data class Error(
    val startOffset: Int,
    val endOffset: Int,

    val lineNumber: Int,
    val columnNumber: Int,

    val errorMessage: String,
    val ruleClass: Class<Rule>
)

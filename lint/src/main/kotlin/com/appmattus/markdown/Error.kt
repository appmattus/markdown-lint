package com.appmattus.markdown

data class Error(val startOffset: Int, val endOffset: Int, val errorMessage: String, val ruleId: String)

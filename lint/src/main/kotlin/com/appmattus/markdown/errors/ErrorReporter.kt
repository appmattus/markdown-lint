package com.appmattus.markdown.errors

import com.appmattus.markdown.processing.MarkdownDocument
import com.appmattus.markdown.rules.Rule

class ErrorReporter(private val ruleClass: Class<Rule>, private val document: MarkdownDocument) {
    private val _errors = mutableSetOf<Error>()

    val errors: List<Error>
        get() = _errors.toList()

    fun reportError(startOffset: Int, endOffset: Int, errorMessage: String) {
        val lineNumber = document.getLineNumber(startOffset) + 1
        val columnNumber = document.getColumnNumber(startOffset) + 1

        _errors.add(
            Error(
                startOffset,
                endOffset,
                lineNumber,
                columnNumber,
                errorMessage,
                ruleClass
            )
        )
    }
}

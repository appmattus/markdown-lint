package com.appmattus.markdown

abstract class Rule(val name: String) {
    abstract val description: String
    abstract val tags: List<String>

    protected abstract fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter)

    fun processDocument(document: MarkdownDocument): List<Error> {
        val errorReporter = ErrorReporter(javaClass, document)
        visitDocument(document, errorReporter)
        return errorReporter.errors
    }

    protected abstract val config: RuleSetup.Builder.() -> Unit

    val configuration
        get() = RuleSetup.Builder().apply(config).build()
}

class ErrorReporter(private val ruleClass: Class<Rule>, private val document: MarkdownDocument) {
    private val _errors = mutableSetOf<Error>()

    val errors: List<Error>
        get() = _errors.toList()

    fun reportError(startOffset: Int, endOffset: Int, errorMessage: String) {
        val lineNumber = document.getLineNumber(startOffset) + 1
        val columnNumber = document.getColumnNumber(startOffset) + 1

        _errors.add(Error(startOffset, endOffset, lineNumber, columnNumber, errorMessage, ruleClass))
    }
}

package com.appmattus.markdown

abstract class Rule(val name: String) {
    abstract val description: String
    abstract val tags: List<String>

    abstract fun visitDocument(document: MarkdownDocument)

    val errors = mutableSetOf<Error>()
    fun reportError(startOffset: Int, endOffset: Int, errorMessage: String) {
        errors.add(Error(startOffset, endOffset, errorMessage, javaClass.simpleName))
    }

    protected abstract val config: RuleSetup.Builder.() -> Unit

    val configuration
        get() = RuleSetup.Builder().apply(config).build()
}

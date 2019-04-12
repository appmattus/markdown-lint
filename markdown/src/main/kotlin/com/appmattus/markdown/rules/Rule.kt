package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.Error
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.processing.MarkdownDocument

abstract class Rule {

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

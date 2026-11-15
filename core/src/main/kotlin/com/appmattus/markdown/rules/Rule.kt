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

    /**
     * The rule's setup as a lambda. Subclasses mark it @Transient so the configuration cache never stores it, as
     * lambdas from a build script cannot be reliably restored; [configuration] holds the resolved value instead.
     */
    protected abstract val config: RuleSetup.Builder.() -> Unit

    private var resolvedConfiguration: RuleSetup? = null

    /**
     * Applies [setup] after the rule's own configuration, resolving the result immediately.
     */
    internal fun withSetup(setup: (RuleSetup.Builder) -> Unit) = apply {
        resolvedConfiguration = RuleSetup.Builder().apply(config).also(setup).build()
    }

    val configuration: RuleSetup
        get() = resolvedConfiguration ?: RuleSetup.Builder().apply(config).build().also { resolvedConfiguration = it }
}

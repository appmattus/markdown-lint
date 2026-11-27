package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.Error
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.processing.MarkdownDocument
import java.io.Serializable

/**
 * Base class for markdown rules. Implement [visitDocument] and report problems with the [ErrorReporter].
 *
 * Pass [setup] to the constructor to let users change `active`, `includes` and `excludes` for the rule, for example
 * `class MyRule(config: RuleSetup.Builder.() -> Unit = {}) : Rule(config)`. A rule without settings can use `Rule()`.
 *
 * The setup is resolved into [configuration] before the Gradle configuration cache is stored, so the lambda itself is
 * kept in a transient field.
 *
 * Rules are [Serializable] so that tools such as the Spotless step can store them. A rule's own fields need to be
 * serializable too, and its setup must be passed to the constructor rather than by overriding [config].
 */
abstract class Rule(@Transient private val setup: RuleSetup.Builder.() -> Unit = {}) : Serializable {

    protected abstract fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter)

    fun processDocument(document: MarkdownDocument): List<Error> {
        val errorReporter = ErrorReporter(javaClass, document)
        visitDocument(document, errorReporter)
        return errorReporter.errors
    }

    /**
     * The rule's setup, which defaults to the setup passed to the constructor. Rules written before the setup could be
     * passed to the constructor override this instead, which is still supported.
     */
    protected open val config: RuleSetup.Builder.() -> Unit
        get() = setup

    private var resolvedConfiguration: RuleSetup? = null

    /**
     * Applies [setup] after the rule's own configuration, resolving the result immediately.
     */
    internal fun withSetup(setup: (RuleSetup.Builder) -> Unit) = apply {
        resolvedConfiguration = RuleSetup.Builder().apply(ownSetup()).also(setup).build()
    }

    val configuration: RuleSetup
        get() = resolvedConfiguration ?: RuleSetup.Builder().apply(ownSetup()).build().also {
            resolvedConfiguration = it
        }

    private fun ownSetup(): RuleSetup.Builder.() -> Unit = config

    private companion object {
        private const val serialVersionUID = 1L
    }
}

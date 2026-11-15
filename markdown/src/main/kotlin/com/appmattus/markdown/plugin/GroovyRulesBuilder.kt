package com.appmattus.markdown.plugin

import com.appmattus.markdown.dsl.MarkdownDsl
import com.appmattus.markdown.dsl.RulesBuilder
import com.appmattus.markdown.rules.Rule
import groovy.lang.Closure

/**
 * The delegate of a Groovy `rules { }` block. Groovy maps unary plus to `positive()` and doesn't set a delegate on
 * closures passed as Kotlin lambdas, so rules are added with [rule] and their setup is given as a [Closure].
 */
@MarkdownDsl
class GroovyRulesBuilder internal constructor(private val builder: RulesBuilder) {

    /**
     * Adds [rule], replacing the default settings for a rule of the same type: `rule(new SingleH1Rule(2))`.
     */
    fun rule(rule: Rule) = builder.rule(rule)

    /**
     * Adds [rule] with its [setup] applied after the rule's own configuration:
     * `rule(new SingleH1Rule()) { active = false }`.
     */
    fun rule(rule: Rule, setup: Closure<*>) = builder.rule(rule) { configureWith(setup) }
}

/**
 * Runs [closure] with this object as its delegate, so properties and methods inside a Groovy closure resolve against
 * this object rather than the build script.
 */
internal fun <T : Any> T.configureWith(closure: Closure<*>): T = apply {
    val copy = closure.clone() as Closure<*>
    copy.resolveStrategy = Closure.DELEGATE_FIRST
    copy.delegate = this
    if (copy.maximumNumberOfParameters == 0) copy.call() else copy.call(this)
}

package com.appmattus.markdown.dsl

import com.appmattus.markdown.rules.Rule
import groovy.lang.Closure

@MarkdownDsl
class RulesBuilder {
    private val rules = mutableListOf<Rule>()

    operator fun Rule.unaryPlus() {
        rules += this
    }

    /**
     * Adds [rule], replacing the default settings for a rule of the same type. Equivalent to `+rule` in Kotlin, for
     * use from Groovy build scripts: `rule(new SingleH1Rule(2))`.
     */
    fun rule(rule: Rule) {
        rules += rule
    }

    /**
     * Adds [rule] with its [setup] applied after the rule's own configuration, for use from Groovy build scripts:
     * `rule(new SingleH1Rule()) { active = false }`.
     */
    fun rule(rule: Rule, setup: Closure<*>) {
        rules += rule.withSetup { builder -> builder.configureWith(setup) }
    }

    internal fun build(): List<Rule> = rules.toList()
}

/**
 * Runs [closure] with this object as its delegate, so properties and methods inside a Groovy closure resolve against
 * this object rather than the build script.
 */
internal fun <T : Any> T.configureWith(closure: Closure<*>): T = apply {
    @Suppress("UNCHECKED_CAST")
    val copy = closure.clone() as Closure<*>
    copy.resolveStrategy = Closure.DELEGATE_FIRST
    copy.delegate = this
    if (copy.maximumNumberOfParameters == 0) copy.call() else copy.call(this)
}

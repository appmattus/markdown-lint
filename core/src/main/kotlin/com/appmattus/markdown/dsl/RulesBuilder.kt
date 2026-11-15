package com.appmattus.markdown.dsl

import com.appmattus.markdown.rules.Rule

@MarkdownDsl
class RulesBuilder {
    private val rules = mutableListOf<Rule>()

    operator fun Rule.unaryPlus() {
        rules += this
    }

    /**
     * Adds [rule], replacing the default settings for a rule of the same type. Equivalent to `+rule`.
     */
    fun rule(rule: Rule) {
        rules += rule
    }

    /**
     * Adds [rule] with its [setup] applied after the rule's own configuration.
     */
    fun rule(rule: Rule, setup: RuleSetup.Builder.() -> Unit) {
        rules += rule.withSetup { builder -> builder.setup() }
    }

    fun build(): List<Rule> = rules.toList()
}

package com.appmattus.markdown.dsl

import com.appmattus.markdown.rules.Rule

@MarkdownDsl
class RulesBuilder {
    private val rules = mutableListOf<Rule>()

    operator fun Rule.unaryPlus() {
        rules += this
    }

    internal fun build(): List<Rule> = rules.toList()
}

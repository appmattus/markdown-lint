package com.appmattus.markdown.dsl

import com.appmattus.markdown.rules.Rule

data class MarkdownLintConfig(val rules: List<Rule>, val reports: Set<Report>) {

    @MarkdownDsl
    class Builder {
        private var reports: Set<Report> = setOf(
            Report.Html,
            Report.Checkstyle
        )
        private var rules: List<Rule> = emptyList()

        fun reports(body: Report.Builder.() -> Unit) = apply {
            reports = Report.Builder().apply(body).build()
        }

        fun rules(body: RulesBuilder.() -> Unit) = apply {
            rules = RulesBuilder().apply(body).build()
        }

        fun build(): MarkdownLintConfig {
            return MarkdownLintConfig(rules, reports)
        }
    }
}

fun markdownlint(body: MarkdownLintConfig.Builder.() -> Unit) = MarkdownLintConfig.Builder().apply(body).build()

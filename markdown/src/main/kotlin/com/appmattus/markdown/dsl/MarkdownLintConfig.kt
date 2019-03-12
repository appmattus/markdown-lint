package com.appmattus.markdown.dsl

import com.appmattus.markdown.rules.Rule

data class MarkdownLintConfig(val rules: List<Rule>, val reports: Set<Report>, val threshold: Int) {

    @MarkdownDsl
    class Builder {
        private var reports: Set<Report> = setOf(
            Report.Html,
            Report.Checkstyle
        )
        private var rules: List<Rule> = emptyList()

        private var threshold: Int = 0

        fun reports(body: Report.Builder.() -> Unit) = apply {
            reports = Report.Builder().apply(body).build()
        }

        fun rules(body: RulesBuilder.() -> Unit) = apply {
            rules = RulesBuilder().apply(body).build()
        }

        fun threshold(threshold: Int) = apply {
            this.threshold = threshold
        }

        fun build(): MarkdownLintConfig {
            return MarkdownLintConfig(rules, reports, threshold)
        }
    }
}

fun markdownLintConfig(body: MarkdownLintConfig.Builder.() -> Unit) = MarkdownLintConfig.Builder().apply(body).build()

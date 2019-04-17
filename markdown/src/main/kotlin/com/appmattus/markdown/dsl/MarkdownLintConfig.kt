package com.appmattus.markdown.dsl

import com.appmattus.markdown.rules.Rule

data class MarkdownLintConfig(
    val rules: List<Rule>,
    val reports: Set<Report>,
    val threshold: Int,
    val includes: List<String>,
    val excludes: List<String>
) {

    @MarkdownDsl
    class Builder {
        private var reports: Set<Report> = setOf(
            Report.Html,
            Report.Checkstyle
        )
        private var rules: List<Rule> = emptyList()

        private var threshold: Int = 0

        private var includes: List<String> = listOf(".*")
        private var excludes: List<String> = emptyList()

        @Suppress("unused")
        fun reports(body: Report.Builder.() -> Unit) = apply {
            reports = Report.Builder().apply(body).build()
        }

        fun rules(body: RulesBuilder.() -> Unit) = apply {
            rules = RulesBuilder().apply(body).build()
        }

        @Suppress("unused")
        fun threshold(threshold: Int) = apply {
            this.threshold = threshold
        }

        @Suppress("unused")
        fun includes(patterns: List<String>) = apply {
            this.includes = patterns
        }

        @Suppress("unused")
        fun excludes(patterns: List<String>) = apply {
            this.excludes = patterns
        }

        fun build(): MarkdownLintConfig {
            return MarkdownLintConfig(rules, reports, threshold, includes, excludes)
        }
    }
}

fun markdownLintConfig(body: MarkdownLintConfig.Builder.() -> Unit) = MarkdownLintConfig.Builder().apply(body).build()

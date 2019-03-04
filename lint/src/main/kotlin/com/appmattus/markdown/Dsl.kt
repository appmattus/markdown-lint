package com.appmattus.markdown

import com.appmattus.markdown.rules.ConsistentHeaderStyleRule
import com.appmattus.markdown.rules.FirstHeaderH1Rule
import com.appmattus.markdown.rules.config.HeaderStyle

@DslMarker
annotation class MarkdownDsl

data class MarkdownLintConfig(val rules: List<Rule>, val reports: Set<Report>) {

    @MarkdownDsl
    class Builder {
        private var reports: Set<Report> = setOf(Report.Html, Report.Checkstyle)
        private var rules: List<Rule> = emptyList()

        fun reports(body: Report.Builder.() -> Unit) = apply {
            reports = Report.Builder().apply(body).build()
        }

        fun rules(body: RulesBuilder.() -> Unit) = apply {
            rules = RulesBuilder().apply(body).build()
        }

        internal fun build(): MarkdownLintConfig {
            return MarkdownLintConfig(rules, reports)
        }
    }
}

class RulesBuilder {
    private val rules = mutableListOf<Rule>()

    operator fun Rule.unaryPlus() {
        rules += this
    }

    internal fun build(): List<Rule> = rules.toList()
}

data class RuleSetup(val active: Boolean) {

    class Builder() {
        var active = true

        internal fun build() = RuleSetup(active)
    }
}

enum class Report {
    Html, Checkstyle;

    @MarkdownDsl
    class Builder {
        private val reports = mutableSetOf<Report>()

        fun html() {
            reports.add(Html)
        }

        fun checkstyle() {
            reports.add(Checkstyle)
        }

        internal fun build(): Set<Report> {
            return reports.toSet()
        }
    }
}


fun markdownlint(body: MarkdownLintConfig.Builder.() -> Unit) = MarkdownLintConfig.Builder().apply(body).build()


fun hi() {

    markdownlint {
        rules {
            +ConsistentHeaderStyleRule(HeaderStyle.Atx)

            +FirstHeaderH1Rule(level = 5) {
                /*suppress {
                    files(
                            "",
                            ""
                    )
                    +"..."
                }*/
                active = true
            }
        }

        reports {
            html()
            checkstyle()
        }
    }
}

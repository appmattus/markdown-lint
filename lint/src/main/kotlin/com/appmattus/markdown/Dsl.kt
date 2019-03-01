package com.appmattus.markdown

import com.appmattus.markdown.rules.config.HeaderStyle
import com.appmattus.markdown.rules.FirstHeaderH1Rule
import com.appmattus.markdown.rules.ConsistentHeaderStyleRule
import kotlin.reflect.KClass

@DslMarker
annotation class MarkdownDsl

data class MarkdownLintConfig(val rules: List<Rule>, val reports: Set<Report>) {

    @MarkdownDsl
    class Builder {
        private var reports: Set<Report> = setOf(Report.Html, Report.Checkstyle)

        fun reports(body: Report.Builder.() -> Unit) = apply {
            reports = Report.Builder().apply(body).build()
        }

        fun rules(body: RuleConfig.Builder.() -> Unit) = apply {
            //reports = RuleConfig.Builder().apply(body).build()
        }

        internal fun build(): MarkdownLintConfig {


            return MarkdownLintConfig(emptyList(), reports)
        }
    }
}

class RuleConfig {
    class Builder {
        private val rules = mutableListOf<Rule>()

        operator fun Rule.unaryPlus() {
            rules += this
        }

        internal fun build() {

        }
    }
}

data class RuleSetup(val ruleClass: KClass<out Rule>, val active: Boolean) {

    class Builder(private val ruleClass: KClass<out Rule>) {
        var active = true


        internal fun build() = RuleSetup(ruleClass, active)
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

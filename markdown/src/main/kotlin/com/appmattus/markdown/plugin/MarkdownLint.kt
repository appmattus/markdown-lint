package com.appmattus.markdown.plugin

import com.appmattus.markdown.dsl.Config
import com.appmattus.markdown.dsl.MarkdownDsl
import com.appmattus.markdown.dsl.Report
import com.appmattus.markdown.dsl.RulesBuilder
import com.appmattus.markdown.rules.Rule
import groovy.lang.Closure

@MarkdownDsl
open class MarkdownLint {

    var rules: List<Rule> = defaults.rules
        private set

    var reports: Set<Report> = defaults.reports
        private set

    var threshold: Int = defaults.threshold
    var includes: List<String> = defaults.includes
    var excludes: List<String> = defaults.excludes

    fun reports(body: Report.Builder.() -> Unit) = apply {
        reports = Report.Builder().apply(body).build()
    }

    fun rules(body: RulesBuilder.() -> Unit) = apply {
        rules = RulesBuilder().apply(body).build()
    }

    /**
     * Groovy build script equivalent of [rules]:
     * ```
     * rules {
     *     rule(new SingleH1Rule()) { active = false }
     * }
     * ```
     */
    fun rules(body: Closure<*>) = apply {
        rules = RulesBuilder().also { GroovyRulesBuilder(it).configureWith(body) }.build()
    }

    /**
     * Groovy build script equivalent of [reports]: `reports { html() }`
     */
    fun reports(body: Closure<*>) = apply {
        reports = Report.Builder().configureWith(body).build()
    }

    internal fun build() =
        Config(rules.toList(), reports.toSet(), threshold, includes.toList(), excludes.toList())

    private companion object {
        val defaults = Config()
    }
}

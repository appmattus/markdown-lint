package com.appmattus.markdown.plugin

import com.appmattus.markdown.dsl.Config
import com.appmattus.markdown.dsl.MarkdownDsl
import com.appmattus.markdown.dsl.Report
import com.appmattus.markdown.dsl.RulesBuilder
import com.appmattus.markdown.dsl.configureWith
import com.appmattus.markdown.rules.Rule
import groovy.lang.Closure

@MarkdownDsl
open class MarkdownLint {

    var rules: List<Rule> = emptyList()
        private set

    var reports: Set<Report> = setOf(Report.Html, Report.Checkstyle)
        private set

    var threshold: Int = 0
    var includes: List<String> = listOf(".*")
    var excludes: List<String> = emptyList()

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
        rules = RulesBuilder().configureWith(body).build()
    }

    /**
     * Groovy build script equivalent of [reports]: `reports { html() }`
     */
    fun reports(body: Closure<*>) = apply {
        reports = Report.Builder().configureWith(body).build()
    }

    internal fun build() =
        Config(rules.toList(), reports.toSet(), threshold, includes.toList(), excludes.toList())
}

package com.appmattus.markdown.dsl

import com.appmattus.markdown.rules.Rule

/**
 * Configuration for a lint run. The defaults match the Gradle plugin's: every rule with its default settings, html
 * and checkstyle reports, a failure threshold of 0 and all files included.
 */
data class Config(
    val rules: List<Rule> = emptyList(),
    val reports: Set<Report> = setOf(Report.Html, Report.Checkstyle),
    val threshold: Int = 0,
    val includes: List<String> = listOf(".*"),
    val excludes: List<String> = emptyList()
)

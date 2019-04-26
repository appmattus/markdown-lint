package com.appmattus.markdown.dsl

import com.appmattus.markdown.rules.Rule

data class Config(
    val rules: List<Rule>,
    val reports: Set<Report>,
    val threshold: Int,
    val includes: List<String>,
    val excludes: List<String>
)

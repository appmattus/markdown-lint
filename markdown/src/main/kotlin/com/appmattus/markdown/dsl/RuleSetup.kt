package com.appmattus.markdown.dsl

data class RuleSetup(
    val active: Boolean,
    val includes: List<String>,
    val excludes: List<String>
) {

    class Builder {
        var active = true
        var includes: List<String> = listOf(".*")
        var excludes: List<String> = emptyList()

        internal fun build() = RuleSetup(active, includes, excludes)
    }
}

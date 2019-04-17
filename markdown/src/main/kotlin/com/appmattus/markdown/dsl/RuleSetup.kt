package com.appmattus.markdown.dsl

data class RuleSetup(
    val active: Boolean,
    val includes: List<String>,
    val excludes: List<String>
) {

    class Builder {
        var active = true
        @Suppress("MemberVisibilityCanBePrivate")
        var includes: List<String> = listOf(".*")
        @Suppress("MemberVisibilityCanBePrivate")
        var excludes: List<String> = emptyList()

        internal fun build() = RuleSetup(active, includes, excludes)
    }
}

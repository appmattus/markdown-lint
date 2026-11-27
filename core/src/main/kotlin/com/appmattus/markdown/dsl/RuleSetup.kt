package com.appmattus.markdown.dsl

import java.io.Serializable

data class RuleSetup(
    val active: Boolean,
    val includes: List<String>,
    val excludes: List<String>
) : Serializable {

    private companion object {
        private const val serialVersionUID = 1L
    }

    class Builder {
        var active = true

        @Suppress("MemberVisibilityCanBePrivate")
        var includes: List<String> = listOf(".*")

        @Suppress("MemberVisibilityCanBePrivate")
        var excludes: List<String> = emptyList()

        internal fun build() = RuleSetup(active, includes, excludes)
    }
}

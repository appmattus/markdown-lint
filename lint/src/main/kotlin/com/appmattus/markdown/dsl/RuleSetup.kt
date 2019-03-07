package com.appmattus.markdown.dsl

data class RuleSetup(val active: Boolean) {

    class Builder {
        var active = true

        internal fun build() = RuleSetup(active)
    }
}

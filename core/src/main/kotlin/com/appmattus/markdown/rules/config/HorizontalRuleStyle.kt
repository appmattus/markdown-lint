package com.appmattus.markdown.rules.config

import java.io.Serializable

// Data objects keep their identity when deserialized, as rules compare against them
sealed class HorizontalRuleStyle : Serializable {
    data object Consistent : HorizontalRuleStyle()
    data object Asterisk : Exact("***")
    data object Dash : Exact("---")
    data object Underscore : Exact("___")
    open class Exact(val chars: String) : HorizontalRuleStyle()

    private companion object {
        private const val serialVersionUID = 1L
    }
}

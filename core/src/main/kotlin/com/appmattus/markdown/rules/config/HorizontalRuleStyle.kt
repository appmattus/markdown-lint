package com.appmattus.markdown.rules.config

sealed class HorizontalRuleStyle {
    object Consistent : HorizontalRuleStyle()
    object Asterisk : Exact("***")
    object Dash : Exact("---")
    object Underscore : Exact("___")
    open class Exact(val chars: String) : HorizontalRuleStyle()
}

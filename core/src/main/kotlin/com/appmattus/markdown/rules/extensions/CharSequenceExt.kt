package com.appmattus.markdown.rules.extensions

private val emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}\$".toRegex(RegexOption.IGNORE_CASE)

val CharSequence.isEmail: Boolean
    get() {
        return emailRegex.matches(this)
    }

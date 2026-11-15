package com.appmattus.markdown.dsl

enum class Report {
    Html, Checkstyle;

    @MarkdownDsl
    class Builder {
        private val reports = mutableSetOf<Report>()

        fun html() {
            reports.add(Html)
        }

        fun checkstyle() {
            reports.add(Checkstyle)
        }

        fun build(): Set<Report> {
            return reports.toSet()
        }
    }
}

package com.appmattus.markdown

import com.android.tools.lint.client.api.IssueRegistry

class MarkdownIssueRegistry : IssueRegistry() {

    override val issues = listOf(MarkdownDetector.ISSUE)
}

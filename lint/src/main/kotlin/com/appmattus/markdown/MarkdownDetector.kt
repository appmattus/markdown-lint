package com.appmattus.markdown

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Context
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.Location
import com.android.tools.lint.detector.api.OtherFileScanner
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.endsWith
import com.appmattus.markdown.rules.NoHardTabsRule


class MarkdownDetector : Detector(), OtherFileScanner {

    override fun run(context: Context) {
        if (endsWith(context.file.name, ".md")) {

            val parser = ParserFactory.parser

            val document = parser.parse(context.getContents().toString())

            NoHardTabsRule().apply {
                visitDocument(MarkdownDocument(context.file.name, document))
            }.errors.map {

                val location = Location.create(context.file, context.getContents(), it.startOffset, it.endOffset)

                context.report(ISSUE, location, "No hard tabs!")

                //println(this)
            }

            //println(context.file.name)
            //println(context.getContents())

        }
    }

    companion object {
        val ISSUE = Issue.create(
            "Enum",
            "Avoid Using Enums",
            "No real Android programmer should ever use enums. EVER.",
            Category.CORRECTNESS,
            5,
            Severity.WARNING,
            Implementation(MarkdownDetector::class.java, Scope.OTHER_SCOPE)
        )
    }
}

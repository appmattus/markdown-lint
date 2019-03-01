package com.appmattus.markdown

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import junit.framework.TestCase
import java.io.InputStream
import java.util.Arrays

class EnumDetectorTest : LintDetectorTest() {

    override fun getDetector(): Detector {
        return MarkdownDetector()
    }

    override fun getIssues(): List<Issue> {
        return Arrays.asList(MarkdownDetector.ISSUE)
    }

    override fun allowMissingSdk() = true

    /**
     * Test that an empty java file has no warnings.
     */
    fun testEmptyCase() {
        val result = lintMarkdown("alternate_top_level_header.md")

        TestCase.assertEquals(NO_WARNINGS, result)
    }

    private fun lintMarkdown(file: String) =
            lintProject(bytes("src/docs/$file", getTestResource(file, true).readBytes()))


    override fun getTestResource(relativePath: String, expectExists: Boolean): InputStream {
        return javaClass.classLoader.getResourceAsStream(relativePath)
    }

    companion object {

        private val NO_WARNINGS = "No warnings."
    }

}

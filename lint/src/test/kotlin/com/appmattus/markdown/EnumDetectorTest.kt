package com.appmattus.markdown

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.android.utils.SdkUtils
import junit.framework.TestCase
import java.io.File
import java.io.InputStream
import java.net.MalformedURLException
import java.util.Arrays

class EnumDetectorTest : LintDetectorTest() {

    private val testDataRootDir: File?
        get() {
            val source = javaClass.protectionDomain.codeSource
            if (source != null) {
                val location = source.location
                try {
                    val classesDir = SdkUtils.urlToFile(location)
                    return classesDir.parentFile.absoluteFile.parentFile.parentFile
                } catch (e: MalformedURLException) {
                    TestCase.fail(e.localizedMessage)
                }

            }
            return null
        }

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
    @Throws(Exception::class)
    fun testEmptyCase() {
        val result = lintMarkdown("alternate_top_level_header.md")

        TestCase.assertEquals(NO_WARNINGS, result)
    }

    private fun lintMarkdown(file: String) =
        lintProject(bytes("src/docs/$file", getTestResource(file, true).readBytes()))

    /**
     * Test that a java file with an enum has a warning.
     */
    /*@Throws(Exception::class)
    fun testEnumCase() {
        val file = "EnumTestCase.java"
        val warningMessage = (file
                + ": Warning: "
                + MarkdownDetector.ISSUE.getBriefDescription(TextFormat.TEXT)
                + " ["
                + MarkdownDetector.ISSUE.id
                + "]\n"
                + "0 errors, 1 warnings\n")
        TestCase.assertEquals(
            warningMessage,
            lintFiles(file)
        )
    }*/

    override fun getTestResource(relativePath: String, expectExists: Boolean): InputStream {
        return javaClass.classLoader.getResourceAsStream(relativePath)
    }

    /*override fun getTestResource(relativePath: String, expectExists: Boolean): InputStream {
        val path = (PATH_TEST_RESOURCES + relativePath).replace('/', File.separatorChar)
        val file = File(testDataRootDir, path)
        if (file.exists()) {
            try {
                return BufferedInputStream(FileInputStream(file))
            } catch (e: FileNotFoundException) {
                if (expectExists) {
                    TestCase.fail("Could not find file $relativePath")
                }
            }

        }
        throw IllegalStateException()
    }*/

    companion object {

        private val PATH_TEST_RESOURCES = "/lint/src/test/resources/enum/"
        private val NO_WARNINGS = "No warnings."
    }

}

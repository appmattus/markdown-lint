package com.appmattus.markdown.plugin

import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import java.io.File
import java.io.InputStream
import kotlin.test.Test

class MarkdownLintPluginTest {
    @get:Rule
    val temporaryFolder = TemporaryFolder()

    private val slash = Regex.escape(File.separator)
    private val htmlReportPattern =
        "build${slash}reports${slash}markdownlint${slash}markdownlint\\.html".toRegex().toPattern()
    private val xmlReportPattern =
        "build${slash}reports${slash}markdownlint${slash}markdownlint\\.xml".toRegex().toPattern()

    @Test(timeout = TIMEOUT)
    fun `no markdown files returns no errors`() {
        lateinit var output: String

        // Given a build script with default configuration
        temporaryFolder.createBuildScriptWithDefaultConfig()

        // When we execute the markdownlint task
        output = build(temporaryFolder, "markdownlint", "-q", "--stacktrace").output.trimEnd()

        // Then no files analysed
        assertThat(output).contains("0 markdown files were analysed")

        // And no errors were reported
        assertThat(output).contains("No errors reported")
    }

    @Test(timeout = TIMEOUT)
    fun `one good markdown file returns no errors`() {
        lateinit var output: String

        // Given a build script with default configuration
        temporaryFolder.createBuildScriptWithDefaultConfig()

        // And a good markdown file
        temporaryFolder.createMarkdownFileWithNoErrors("README.md")

        // When we execute the markdownlint task
        output = build(temporaryFolder, "markdownlint", "-q", "--stacktrace").output.trimEnd()

        // Then one file analysed
        assertThat(output).contains("1 markdown files were analysed")

        // And no errors were reported
        assertThat(output).contains("No errors reported")
    }

    @Test(timeout = TIMEOUT)
    fun `markdown file in build directory is ignored`() {
        lateinit var output: String

        // Given a build script with default configuration
        temporaryFolder.createBuildScriptWithDefaultConfig()

        // And a good markdown file
        temporaryFolder.createMarkdownFileWithNoErrors("README.md")

        // And a good markdown file in build directory
        temporaryFolder.newFolder("build")
        temporaryFolder.createMarkdownFileWithNoErrors("build/README.md")

        // When we execute the markdownlint task
        output = build(temporaryFolder, "markdownlint", "-q", "--stacktrace").output.trimEnd()

        // Then one file analysed
        assertThat(output).contains("1 markdown files were analysed")

        // And no errors were reported
        assertThat(output).contains("No errors reported")
    }

    @Test(timeout = TIMEOUT)
    fun `markdown file in random directory is found`() {
        lateinit var output: String

        // Given a build script with default configuration
        temporaryFolder.createBuildScriptWithDefaultConfig()

        // And a good markdown file
        temporaryFolder.createMarkdownFileWithNoErrors("README.md")

        // And a good markdown file in random directory
        temporaryFolder.newFolder("random")
        temporaryFolder.createMarkdownFileWithNoErrors("random/README.md")

        // When we execute the markdownlint task
        output = build(temporaryFolder, "markdownlint", "-q", "--stacktrace").output.trimEnd()

        // Then two file analysed
        assertThat(output).contains("2 markdown files were analysed")

        // And no errors were reported
        assertThat(output).contains("No errors reported")
    }

    @Test(timeout = TIMEOUT)
    fun `default config generates both xml and html reports`() {
        lateinit var output: String

        // Given a build script with default configuration
        temporaryFolder.createBuildScriptWithDefaultConfig()

        // When we execute the markdownlint task
        output = build(temporaryFolder, "markdownlint", "-q", "--stacktrace").output.trimEnd()

        // Then xml report generated
        assertThat(output).contains("Successfully generated Checkstyle XML report")
        assertThat(output).containsPattern(xmlReportPattern)

        // And html report generated
        assertThat(output).contains("Successfully generated HTML report")
        assertThat(output).containsPattern(htmlReportPattern)
    }

    @Test(timeout = TIMEOUT)
    fun `empty report config generates no reports`() {
        lateinit var output: String

        // Given a build script with config disabling reports
        temporaryFolder.createBuildScriptWithNoReports()

        // When we execute the markdownlint task
        output = build(temporaryFolder, "markdownlint", "-q", "--stacktrace").output.trimEnd()

        // Then no xml report generated
        assertThat(output).doesNotContain("Successfully generated Checkstyle XML report")
        assertThat(output).doesNotContainPattern(xmlReportPattern)

        // And no html report generated
        assertThat(output).doesNotContain("Successfully generated HTML report")
        assertThat(output).doesNotContainPattern(htmlReportPattern)
    }

    @Test(timeout = TIMEOUT)
    fun `html only report config generates only html report`() {
        lateinit var output: String

        // Given a build script with html only
        temporaryFolder.createBuildScriptWithHtmlReportOnly()

        // When we execute the markdownlint task
        output = build(temporaryFolder, "markdownlint", "-q", "--stacktrace").output.trimEnd()

        // Then no xml report generated
        assertThat(output).doesNotContain("Successfully generated Checkstyle XML report")
        assertThat(output).doesNotContainPattern(xmlReportPattern)

        // And html report generated
        assertThat(output).contains("Successfully generated HTML report")
        assertThat(output).containsPattern(htmlReportPattern)
    }

    @Test(timeout = TIMEOUT)
    fun `xml only report config generates only xml report`() {
        lateinit var output: String

        // Given a build script with xml only
        temporaryFolder.createBuildScriptWithXmlReportOnly()

        // When we execute the markdownlint task
        output = build(temporaryFolder, "markdownlint", "-q", "--stacktrace").output.trimEnd()

        // Then xml report generated
        assertThat(output).contains("Successfully generated Checkstyle XML report")
        assertThat(output).containsPattern(xmlReportPattern)

        // And no html report generated
        assertThat(output).doesNotContain("Successfully generated HTML report")
        assertThat(output).doesNotContainPattern(htmlReportPattern)
    }

    @Test(timeout = TIMEOUT)
    fun `one bad markdown file returns one error`() {
        lateinit var output: String

        // Given a build script with default configuration
        temporaryFolder.createBuildScriptWithDefaultConfig()

        // And a bad markdown file
        temporaryFolder.createMarkdownFileWithAnError("README.md")

        // When we execute the markdownlint task
        output = buildAndFail(temporaryFolder, "markdownlint", "-q", "--stacktrace").output.trimEnd()

        // Then one error is reported
        val errors = Regex("SingleH1Rule").findAll(output).toList()
        assertThat(errors.size).isOne()

        // And xml contains one error
        val xmlReport =
            File(temporaryFolder.root, "build/reports/markdownlint/markdownlint.xml").readText().trim()
        val xmlErrors = Regex("<error").findAll(xmlReport).toList()
        assertThat(xmlErrors.size).isOne()
    }

    @Test(timeout = TIMEOUT)
    fun `two bad markdown files with one in build directory returns one error`() {
        lateinit var output: String

        // Given a build script with default configuration
        temporaryFolder.createBuildScriptWithDefaultConfig()

        // And a bad markdown file
        temporaryFolder.createMarkdownFileWithAnError("README.md")

        // And a bad markdown file in the build directory
        temporaryFolder.newFolder("build")
        temporaryFolder.createMarkdownFileWithAnError("build/README.md")

        // When we execute the markdownlint task
        output = buildAndFail(temporaryFolder, "markdownlint", "-q", "--stacktrace").output.trimEnd()

        // Then one error is reported
        val errors = Regex("SingleH1Rule").findAll(output).toList()
        assertThat(errors.size).isOne()

        // And xml contains one error
        val xmlReport =
            File(temporaryFolder.root, "build/reports/markdownlint/markdownlint.xml").readText().trim()
        val xmlErrors = Regex("<error").findAll(xmlReport).toList()
        assertThat(xmlErrors.size).isOne()
    }

    @Test(timeout = TIMEOUT)
    fun `configuration changes errors reported`() {
        lateinit var output: String

        // Given a build script with config disabling rule
        temporaryFolder.createBuildScriptWithConfigDisablingRule()

        // And a bad markdown file
        temporaryFolder.createMarkdownFileWithAnError("README.md")

        // When we execute the markdownlint task
        output = build(temporaryFolder, "markdownlint", "-q", "--stacktrace").output.trimEnd()

        // Then one file analysed
        assertThat(output).contains("1 markdown files were analysed")

        // And no errors were reported
        assertThat(output).contains("No errors reported")

        // And xml contains no error
        val xmlReport =
            File(temporaryFolder.root, "build/reports/markdownlint/markdownlint.xml").readText().trim()
        val errors = Regex("<error").findAll(xmlReport).toList()
        assertThat(errors.size).isZero()
    }

    @Test(timeout = TIMEOUT)
    fun `one bad markdown file throws an exception`() {
        lateinit var output: String

        // Given a build script with default configuration
        temporaryFolder.createBuildScriptWithDefaultConfig()

        // And a bad markdown file
        temporaryFolder.createMarkdownFileWithAnError("README.md")

        // When we execute the markdownlint task
        output = buildAndFail(temporaryFolder, "markdownlint", "-q", "--stacktrace").output.trimEnd()

        // Then one error is reported
        assertThat(output).contains("Build failure threshold of 0 reached with 1 errors!")
    }

    @Test(timeout = TIMEOUT)
    fun `one bad markdown file and adjusted threshold doesn't throw an exception`() {
        lateinit var output: String

        // Given a build script with config increasing threshold
        temporaryFolder.createBuildScriptWithIncreasedThreshold()

        // And a bad markdown file
        temporaryFolder.createMarkdownFileWithAnError("README.md")

        // When we execute the markdownlint task
        output = build(temporaryFolder, "markdownlint", "-q", "--stacktrace").output.trimEnd()

        // Then no errors reported
        assertThat(output).doesNotContain("Build failure")
    }

    @Test(timeout = TIMEOUT)
    fun `task is compatible with the configuration cache`() {
        lateinit var firstOutput: String
        lateinit var secondOutput: String

        // Given a build script with a rule disabled through its configuration lambda
        temporaryFolder.createBuildScriptWithConfigDisablingRule()

        // And a markdown file that would break the disabled rule
        temporaryFolder.createMarkdownFileWithAnError("README.md")

        // When we execute the markdownlint task twice with the configuration cache
        firstOutput = build(temporaryFolder, "markdownlint", "--configuration-cache", "--stacktrace").output
        secondOutput = build(temporaryFolder, "markdownlint", "--configuration-cache", "--stacktrace").output

        // Then the configuration cache entry is stored
        assertThat(firstOutput).contains("Configuration cache entry stored")

        // And the configuration cache entry is reused
        assertThat(secondOutput).contains("Configuration cache entry reused")

        // And the rule configuration is kept when the configuration cache is reused
        assertThat(firstOutput).contains("No errors reported")
        assertThat(secondOutput).contains("No errors reported")
    }

    @Test(timeout = TIMEOUT)
    fun `groovy build script configures rules, filters and reports`() {
        lateinit var output: String

        // Given a groovy build script that disables a rule, filters files and generates only the html report
        temporaryFolder.createGroovyBuildScript(
            """
            markdownlint {
                rules {
                    rule(new ConsistentHeaderStyleRule(HeaderStyle.Atx))

                    rule(new SingleH1Rule()) {
                        active = false
                    }
                }

                includes = ['.*/docs/.*']
                excludes = ['.*/excluded.md']

                reports {
                    html()
                }

                threshold = 10
            }
            """
        )

        // And bad markdown files inside and outside the included directory
        temporaryFolder.newFolder("docs")
        temporaryFolder.createMarkdownFileWithAnError("docs/README.md")
        temporaryFolder.createMarkdownFileWithAnError("docs/excluded.md")
        temporaryFolder.createMarkdownFileWithAnError("README.md")

        // When we execute the markdownlint task
        output = build(temporaryFolder, "markdownlint", "--stacktrace").output

        // Then only the included file is analysed
        assertThat(output).contains("1 markdown files were analysed")

        // And the disabled rule reports no errors
        assertThat(output).contains("No errors reported")

        // And only the html report is generated
        assertThat(File(temporaryFolder.root, "build/reports/markdownlint/markdownlint.html")).exists()
        assertThat(File(temporaryFolder.root, "build/reports/markdownlint/markdownlint.xml")).doesNotExist()
    }

    @Test(timeout = TIMEOUT)
    fun `groovy rule setup applies to that rule only`() {
        lateinit var output: String

        // Given a groovy build script that limits one rule to the docs directory
        temporaryFolder.createGroovyBuildScript(
            """
            markdownlint {
                rules {
                    rule(new SingleH1Rule()) {
                        includes = ['.*/docs/.*']
                    }
                }
            }
            """
        )

        // And bad markdown files inside and outside the docs directory
        temporaryFolder.newFolder("docs")
        temporaryFolder.createMarkdownFileWithAnError("docs/README.md")
        temporaryFolder.createMarkdownFileWithAnError("README.md")

        // When we execute the markdownlint task
        output = buildAndFail(temporaryFolder, "markdownlint", "--stacktrace").output

        // Then both files are analysed
        assertThat(output).contains("2 markdown files were analysed")

        // And the rule only reports the file in the docs directory
        val errors = Regex("SingleH1Rule at .*").findAll(output).map { it.value }.toList()
        assertThat(errors).hasSize(1)
        assertThat(errors.single()).contains("docs")
    }

    @Test(timeout = TIMEOUT)
    fun `groovy rule setup with an unknown property fails when configuring`() {
        lateinit var output: String

        // Given a groovy build script with a misspelt rule setup property
        temporaryFolder.createGroovyBuildScript(
            """
            markdownlint {
                rules {
                    rule(new SingleH1Rule()) {
                        activ = false
                    }
                }
            }
            """
        )

        // When we execute the markdownlint task
        output = buildAndFail(temporaryFolder, "markdownlint", "--stacktrace").output

        // Then the build script fails to evaluate
        assertThat(output).contains("A problem occurred evaluating root project")

        // And the correct property is suggested
        assertThat(output).contains("No such property: activ")
        assertThat(output).contains("Possible solutions: active")
    }

    private companion object {
        const val TIMEOUT = 60_000L
    }
}

private fun TemporaryFolder.createBuildScriptWithDefaultConfig() = createFile("build.gradle.kts") {
    """
    plugins {
        id("com.appmattus.markdown")
    }
    """.trimIndent()
}

private fun TemporaryFolder.createGroovyBuildScript(markdownlint: String) = createFile("build.gradle") {
    """
    |import com.appmattus.markdown.rules.ConsistentHeaderStyleRule
    |import com.appmattus.markdown.rules.SingleH1Rule
    |import com.appmattus.markdown.rules.config.HeaderStyle
    |
    |plugins {
    |    id 'com.appmattus.markdown'
    |}
    |
    """.trimMargin() + markdownlint.trimIndent()
}

private fun TemporaryFolder.createBuildScriptWithNoReports() = createFile("build.gradle.kts") {
    """
    plugins {
        id("com.appmattus.markdown")
    }
    markdownlint {
        reports {
        }
    }
    """.trimIndent()
}

private fun TemporaryFolder.createBuildScriptWithHtmlReportOnly() = createFile("build.gradle.kts") {
    """
    plugins {
        id("com.appmattus.markdown")
    }
    markdownlint {
        reports {
            html()
        }
    }
    """.trimIndent()
}

private fun TemporaryFolder.createBuildScriptWithXmlReportOnly() = createFile("build.gradle.kts") {
    """
    plugins {
        id("com.appmattus.markdown")
    }
    markdownlint {
        reports {
            checkstyle()
        }
    }
    """.trimIndent()
}

private fun TemporaryFolder.createBuildScriptWithConfigDisablingRule() = createFile("build.gradle.kts") {
    """
    import com.appmattus.markdown.rules.SingleH1Rule

    plugins {
        id("com.appmattus.markdown")
    }
    markdownlint {
        rules {
            +SingleH1Rule {
                active = false
            }
        }
    }
    """.trimIndent()
}

private fun TemporaryFolder.createBuildScriptWithIncreasedThreshold() = createFile("build.gradle.kts") {
    """
    import com.appmattus.markdown.rules.SingleH1Rule

    plugins {
        id("com.appmattus.markdown")
    }
    markdownlint {
        threshold = 1
    }
    """.trimIndent()
}

private fun TemporaryFolder.createMarkdownFileWithNoErrors(filename: String) = createFile(filename) {
    """
    # Welcome to my project

    This is the introduction

    ## Section 2

    This is the next section
    """.trimIndent() + "\n"
}

private fun TemporaryFolder.createMarkdownFileWithAnError(filename: String) = createFile(filename) {
    """
    # Welcome to my project

    This is the introduction

    # Section 2

    This is the next section
    """.trimIndent() + "\n"
}

private fun build(temporaryFolder: TemporaryFolder, vararg arguments: String): BuildResult =
    GradleRunner
        .create()
        .withProjectDir(temporaryFolder.root)
        .withPluginClasspath()
        .withArguments(*arguments)
        .withJaCoCo()
        .build()

private fun buildAndFail(temporaryFolder: TemporaryFolder, vararg arguments: String): BuildResult =
    GradleRunner
        .create()
        .withProjectDir(temporaryFolder.root)
        .withPluginClasspath()
        .withArguments(*arguments)
        .withJaCoCo()
        .buildAndFail()

private fun TemporaryFolder.createFile(filename: String, content: () -> String): File {
    return newFile(filename).apply {
        writeText(content())
    }
}

private fun InputStream.toFile(file: File) {
    use { input ->
        file.outputStream().use { input.copyTo(it) }
    }
}

private fun GradleRunner.withJaCoCo(): GradleRunner {
    javaClass.classLoader.getResourceAsStream("testkit-gradle.properties").toFile(File(projectDir, "gradle.properties"))
    return this
}

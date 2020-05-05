package com.appmattus.markdown.plugin

import com.appmattus.gherkin.dsl.And
import com.appmattus.gherkin.dsl.Given
import com.appmattus.gherkin.dsl.Then
import com.appmattus.gherkin.dsl.When
import com.appmattus.gherkin.jupiter.gherkin
import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.TestFactory
import org.junit.rules.TemporaryFolder
import java.io.File
import java.io.InputStream

class MarkdownLintPluginTest {
    private val temporaryFolder = TemporaryFolder().apply {
        create()
    }

    private val slash = Regex.escape(File.separator)
    private val htmlReportPattern =
        "build${slash}reports${slash}markdownlint${slash}markdownlint\\.html".toRegex().toPattern()
    private val xmlReportPattern =
        "build${slash}reports${slash}markdownlint${slash}markdownlint\\.xml".toRegex().toPattern()

    @TestFactory
    fun `no markdown files returns no errors`() = gherkin {
        val output: String

        Given("a build script with default configuration") {
            temporaryFolder.createBuildScriptWithDefaultConfig()
        }

        When("we execute the markdownlint task") {
            output = build(temporaryFolder, "markdownlint", "-q", "--stacktrace").output.trimEnd()
        }

        Then("no files analysed") {
            assertThat(output).contains("0 markdown files were analysed")
        }

        And("no errors were reported") {
            assertThat(output).contains("No errors reported")
        }
    }

    @TestFactory
    fun `one good markdown file returns no errors`() = gherkin {
        val output: String

        Given("a build script with default configuration") {
            temporaryFolder.createBuildScriptWithDefaultConfig()
        }

        And("a good markdown file") {
            temporaryFolder.createMarkdownFileWithNoErrors("README.md")
        }

        When("we execute the markdownlint task") {
            output = build(temporaryFolder, "markdownlint", "-q", "--stacktrace").output.trimEnd()
        }

        Then("one file analysed") {
            assertThat(output).contains("1 markdown files were analysed")
        }

        And("no errors were reported") {
            assertThat(output).contains("No errors reported")
        }
    }

    @TestFactory
    fun `markdown file in build directory is ignored`() = gherkin {
        val output: String

        Given("a build script with default configuration") {
            temporaryFolder.createBuildScriptWithDefaultConfig()
        }

        And("a good markdown file") {
            temporaryFolder.createMarkdownFileWithNoErrors("README.md")
        }

        And("a good markdown file in build directory") {
            temporaryFolder.newFolder("build")
            temporaryFolder.createMarkdownFileWithNoErrors("build/README.md")
        }

        When("we execute the markdownlint task") {
            output = build(temporaryFolder, "markdownlint", "-q", "--stacktrace").output.trimEnd()
        }

        Then("one file analysed") {
            assertThat(output).contains("1 markdown files were analysed")
        }

        And("no errors were reported") {
            assertThat(output).contains("No errors reported")
        }
    }

    @TestFactory
    fun `markdown file in random directory is found`() = gherkin {
        val output: String

        Given("a build script with default configuration") {
            temporaryFolder.createBuildScriptWithDefaultConfig()
        }

        And("a good markdown file") {
            temporaryFolder.createMarkdownFileWithNoErrors("README.md")
        }

        And("a good markdown file in random directory") {
            temporaryFolder.newFolder("random")
            temporaryFolder.createMarkdownFileWithNoErrors("random/README.md")
        }

        When("we execute the markdownlint task") {
            output = build(temporaryFolder, "markdownlint", "-q", "--stacktrace").output.trimEnd()
        }

        Then("two file analysed") {
            assertThat(output).contains("2 markdown files were analysed")
        }

        And("no errors were reported") {
            assertThat(output).contains("No errors reported")
        }
    }

    @TestFactory
    fun `default config generates both xml and html reports`() = gherkin {
        val output: String

        Given("a build script with default configuration") {
            temporaryFolder.createBuildScriptWithDefaultConfig()
        }

        When("we execute the markdownlint task") {
            output = build(temporaryFolder, "markdownlint", "-q", "--stacktrace").output.trimEnd()
        }

        Then("xml report generated") {
            assertThat(output).contains("Successfully generated Checkstyle XML report")
            assertThat(output).containsPattern(xmlReportPattern)
        }

        And("html report generated") {
            assertThat(output).contains("Successfully generated HTML report")
            assertThat(output).containsPattern(htmlReportPattern)
        }
    }

    @TestFactory
    fun `empty report config generates no reports`() = gherkin {
        val output: String

        Given("a build script with config disabling reports") {
            temporaryFolder.createBuildScriptWithNoReports()
        }

        When("we execute the markdownlint task") {
            output = build(temporaryFolder, "markdownlint", "-q", "--stacktrace").output.trimEnd()
        }

        Then("no xml report generated") {
            assertThat(output).doesNotContain("Successfully generated Checkstyle XML report")
            assertThat(output).doesNotContainPattern(xmlReportPattern)
        }

        And("no html report generated") {
            assertThat(output).doesNotContain("Successfully generated HTML report")
            assertThat(output).doesNotContainPattern(htmlReportPattern)
        }
    }

    @TestFactory
    fun `html only report config generates only html report`() = gherkin {
        val output: String

        Given("a build script with html only") {
            temporaryFolder.createBuildScriptWithHtmlReportOnly()
        }

        When("we execute the markdownlint task") {
            output = build(temporaryFolder, "markdownlint", "-q", "--stacktrace").output.trimEnd()
        }

        Then("no xml report generated") {
            assertThat(output).doesNotContain("Successfully generated Checkstyle XML report")
            assertThat(output).doesNotContainPattern(xmlReportPattern)
        }

        And("html report generated") {
            assertThat(output).contains("Successfully generated HTML report")
            assertThat(output).containsPattern(htmlReportPattern)
        }
    }

    @TestFactory
    fun `xml only report config generates only xml report`() = gherkin {
        val output: String

        Given("a build script with xml only") {
            temporaryFolder.createBuildScriptWithXmlReportOnly()
        }

        When("we execute the markdownlint task") {
            output = build(temporaryFolder, "markdownlint", "-q", "--stacktrace").output.trimEnd()
        }

        Then("xml report generated") {
            assertThat(output).contains("Successfully generated Checkstyle XML report")
            assertThat(output).containsPattern(xmlReportPattern)
        }

        And("no html report generated") {
            assertThat(output).doesNotContain("Successfully generated HTML report")
            assertThat(output).doesNotContainPattern(htmlReportPattern)
        }
    }

    @TestFactory
    fun `one bad markdown file returns one error`() = gherkin {
        val output: String

        Given("a build script with default configuration") {
            temporaryFolder.createBuildScriptWithDefaultConfig()
        }

        And("a bad markdown file") {
            temporaryFolder.createMarkdownFileWithAnError("README.md")
        }

        When("we execute the markdownlint task") {
            output = buildAndFail(temporaryFolder, "markdownlint", "-q", "--stacktrace").output.trimEnd()
        }

        Then("one error is reported") {
            val errors = Regex("SingleH1Rule").findAll(output).toList()
            assertThat(errors.size).isOne()
        }

        And("xml contains one error") {
            val xmlReport =
                File(temporaryFolder.root, "build/reports/markdownlint/markdownlint.xml").readText().trim()
            val errors = Regex("<error").findAll(xmlReport).toList()
            assertThat(errors.size).isOne()
        }
    }

    @TestFactory
    fun `two bad markdown files with one in build directory returns one error`() = gherkin {
        val output: String

        Given("a build script with default configuration") {
            temporaryFolder.createBuildScriptWithDefaultConfig()
        }

        And("a bad markdown file") {
            temporaryFolder.createMarkdownFileWithAnError("README.md")
        }

        And("a bad markdown file in the build directory") {
            temporaryFolder.newFolder("build")
            temporaryFolder.createMarkdownFileWithAnError("build/README.md")
        }

        When("we execute the markdownlint task") {
            output = buildAndFail(temporaryFolder, "markdownlint", "-q", "--stacktrace").output.trimEnd()
        }

        Then("one error is reported") {
            val errors = Regex("SingleH1Rule").findAll(output).toList()
            assertThat(errors.size).isOne()
        }

        And("xml contains one error") {
            val xmlReport =
                File(temporaryFolder.root, "build/reports/markdownlint/markdownlint.xml").readText().trim()
            val errors = Regex("<error").findAll(xmlReport).toList()
            assertThat(errors.size).isOne()
        }
    }

    @TestFactory
    fun `configuration changes errors reported`() = gherkin {
        val output: String

        Given("a build script with config disabling rule") {
            temporaryFolder.createBuildScriptWithConfigDisablingRule()
        }

        And("a bad markdown file") {
            temporaryFolder.createMarkdownFileWithAnError("README.md")
        }

        When("we execute the markdownlint task") {
            output = build(temporaryFolder, "markdownlint", "-q", "--stacktrace").output.trimEnd()
        }

        Then("one file analysed") {
            assertThat(output).contains("1 markdown files were analysed")
        }

        And("no errors were reported") {
            assertThat(output).contains("No errors reported")
        }

        And("xml contains no error") {
            val xmlReport =
                File(temporaryFolder.root, "build/reports/markdownlint/markdownlint.xml").readText().trim()
            val errors = Regex("<error").findAll(xmlReport).toList()
            assertThat(errors.size).isZero()
        }
    }

    @TestFactory
    fun `one bad markdown file throws an exception`() = gherkin {
        val output: String

        Given("a build script with default configuration") {
            temporaryFolder.createBuildScriptWithDefaultConfig()
        }

        And("a bad markdown file") {
            temporaryFolder.createMarkdownFileWithAnError("README.md")
        }

        When("we execute the markdownlint task") {
            output = buildAndFail(temporaryFolder, "markdownlint", "-q", "--stacktrace").output.trimEnd()
        }

        Then("one error is reported") {
            assertThat(output).contains("Build failure threshold of 0 reached with 1 errors!")
        }
    }

    @TestFactory
    fun `one bad markdown file and adjusted threshold doesn't throw an exception`() = gherkin {
        val output: String

        Given("a build script with config increasing threshold") {
            temporaryFolder.createBuildScriptWithIncreasedThreshold()
        }

        And("a bad markdown file") {
            temporaryFolder.createMarkdownFileWithAnError("README.md")
        }

        When("we execute the markdownlint task") {
            output = build(temporaryFolder, "markdownlint", "-q", "--stacktrace").output.trimEnd()
        }

        Then("no errors reported") {
            assertThat(output).doesNotContain("Build failure")
        }
    }
}

private fun TemporaryFolder.createBuildScriptWithDefaultConfig() = createFile("build.gradle.kts") {
    """
    plugins {
        id("com.appmattus.markdown")
    }
    """.trimIndent()
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

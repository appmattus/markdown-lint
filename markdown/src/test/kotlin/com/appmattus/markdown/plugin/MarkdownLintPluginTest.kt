package com.appmattus.markdown.plugin

import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.rules.TemporaryFolder
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.io.File
import java.io.InputStream
import java.util.concurrent.TimeUnit

object MarkdownLintPluginTest : Spek({
    val timeout: Long = TimeUnit.MINUTES.toMillis(1)

    Feature("MarkdownLintPlugin") {
        val temporaryFolder by memoized {
            TemporaryFolder().apply {
                create()
            }
        }

        val slash = Regex.escape(File.separator)
        val htmlReportPattern =
            "build${slash}reports${slash}markdownlint${slash}markdownlint\\.html".toRegex().toPattern()
        val xmlReportPattern =
            "build${slash}reports${slash}markdownlint${slash}markdownlint\\.xml".toRegex().toPattern()

        Scenario("no markdown files returns no errors") {
            lateinit var output: String

            Given("a build script with default configuration") {
                temporaryFolder.createBuildScriptWithDefaultConfig()
            }

            When("we execute the markdownlint task", timeout) {
                output = build(temporaryFolder, "markdownlint", "-q", "--stacktrace").output.trimEnd()
            }

            Then("no files analysed") {
                assertThat(output).contains("0 markdown files were analysed")
            }

            And("no errors were reported") {
                assertThat(output).contains("No errors reported")
            }
        }

        Scenario("one good markdown file returns no errors") {
            lateinit var output: String

            Given("a build script with default configuration") {
                temporaryFolder.createBuildScriptWithDefaultConfig()
            }

            And("a good markdown file") {
                temporaryFolder.createMarkdownFileWithNoErrors("README.md")
            }

            When("we execute the markdownlint task", timeout) {
                output = build(temporaryFolder, "markdownlint", "-q", "--stacktrace").output.trimEnd()
            }

            Then("one file analysed") {
                assertThat(output).contains("1 markdown files were analysed")
            }

            And("no errors were reported") {
                assertThat(output).contains("No errors reported")
            }
        }

        Scenario("markdown file in build directory is ignored") {
            lateinit var output: String

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

            When("we execute the markdownlint task", timeout) {
                output = build(temporaryFolder, "markdownlint", "-q", "--stacktrace").output.trimEnd()
            }

            Then("one file analysed") {
                assertThat(output).contains("1 markdown files were analysed")
            }

            And("no errors were reported") {
                assertThat(output).contains("No errors reported")
            }
        }

        Scenario("markdown file in random directory is found") {
            lateinit var output: String

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

            When("we execute the markdownlint task", timeout) {
                output = build(temporaryFolder, "markdownlint", "-q", "--stacktrace").output.trimEnd()
            }

            Then("two file analysed") {
                assertThat(output).contains("2 markdown files were analysed")
            }

            And("no errors were reported") {
                assertThat(output).contains("No errors reported")
            }
        }

        Scenario("default config generates both xml and html reports") {
            lateinit var output: String

            Given("a build script with default configuration") {
                temporaryFolder.createBuildScriptWithDefaultConfig()
            }

            When("we execute the markdownlint task", timeout) {
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

        Scenario("empty report config generates no reports") {
            lateinit var output: String

            Given("a build script with config disabling reports") {
                temporaryFolder.createBuildScriptWithNoReports()
            }

            When("we execute the markdownlint task", timeout) {
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

        Scenario("html only report config generates only html report") {
            lateinit var output: String

            Given("a build script with html only") {
                temporaryFolder.createBuildScriptWithHtmlReportOnly()
            }

            When("we execute the markdownlint task", timeout) {
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

        Scenario("xml only report config generates only xml report") {
            lateinit var output: String

            Given("a build script with xml only") {
                temporaryFolder.createBuildScriptWithXmlReportOnly()
            }

            When("we execute the markdownlint task", timeout) {
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

        Scenario("one bad markdown file returns one error") {
            lateinit var output: String

            Given("a build script with default configuration") {
                temporaryFolder.createBuildScriptWithDefaultConfig()
            }

            And("a bad markdown file") {
                temporaryFolder.createMarkdownFileWithAnError("README.md")
            }

            When("we execute the markdownlint task", timeout) {
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

        Scenario("two bad markdown files with one in build directory returns one error") {
            lateinit var output: String

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

            When("we execute the markdownlint task", timeout) {
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

        Scenario("configuration changes errors reported") {
            lateinit var output: String

            Given("a build script with config disabling rule") {
                temporaryFolder.createBuildScriptWithConfigDisablingRule()
            }

            And("a bad markdown file") {
                temporaryFolder.createMarkdownFileWithAnError("README.md")
            }

            When("we execute the markdownlint task", timeout) {
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

        Scenario("one bad markdown file throws an exception") {
            lateinit var output: String

            Given("a build script with default configuration") {
                temporaryFolder.createBuildScriptWithDefaultConfig()
            }

            And("a bad markdown file") {
                temporaryFolder.createMarkdownFileWithAnError("README.md")
            }

            When("we execute the markdownlint task", timeout) {
                output = buildAndFail(temporaryFolder, "markdownlint", "-q", "--stacktrace").output.trimEnd()
            }

            Then("one error is reported") {
                assertThat(output).contains("Build failure threshold of 0 reached with 1 errors!")
            }
        }

        Scenario("one bad markdown file and adjusted threshold doesn't throw an exception") {
            lateinit var output: String

            Given("a build script with config increasing threshold") {
                temporaryFolder.createBuildScriptWithIncreasedThreshold()
            }

            And("a bad markdown file") {
                temporaryFolder.createMarkdownFileWithAnError("README.md")
            }

            When("we execute the markdownlint task", timeout) {
                output = build(temporaryFolder, "markdownlint", "-q", "--stacktrace").output.trimEnd()
            }

            Then("no errors reported") {
                assertThat(output).doesNotContain("Build failure")
            }
        }
    }
})

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

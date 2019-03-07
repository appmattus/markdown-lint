package com.appmattus.markdown.plugin

import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.rules.TemporaryFolder
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.io.File

object MarkdownLintPluginTest : Spek({
    Feature("MarkdownLintPlugin") {
        val temporaryFolder by memoized {
            TemporaryFolder().apply {
                create()
            }
        }

        Scenario("no markdown files returns no errors") {
            lateinit var output: String

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

        Scenario("one good markdown file returns no errors") {
            lateinit var output: String

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

        Scenario("default config generates both xml and html reports") {
            lateinit var output: String

            Given("a build script with default configuration") {
                temporaryFolder.createBuildScriptWithDefaultConfig()
            }

            When("we execute the markdownlint task") {
                output = build(temporaryFolder, "markdownlint", "-q", "--stacktrace").output.trimEnd()
            }

            Then("xml report generated") {
                assertThat(output).contains(
                    "Successfully generated Checkstyle XML report",
                    "build/reports/markdownlint/markdownlint.xml"
                )
            }

            And("html report generated") {
                assertThat(output).contains(
                    "Successfully generated HTML report",
                    "build/reports/markdownlint/markdownlint.html"
                )
            }
        }

        Scenario("empty report config generates no reports") {
            lateinit var output: String

            Given("a build script with default configuration") {
                temporaryFolder.createBuildScriptWithConfigFile()
            }

            And("config file disabling reports") {
                temporaryFolder.createPluginConfigurationWithNoReports()
            }

            When("we execute the markdownlint task") {
                output = build(temporaryFolder, "markdownlint", "-q", "--stacktrace").output.trimEnd()
            }

            Then("no xml report generated") {
                assertThat(output).doesNotContain(
                    "Successfully generated Checkstyle XML report",
                    "build/reports/markdownlint/markdownlint.xml"
                )
            }

            And("no html report generated") {
                assertThat(output).doesNotContain(
                    "Successfully generated HTML report",
                    "build/reports/markdownlint/markdownlint.html"
                )
            }
        }

        Scenario("html only report config generates only html report") {
            lateinit var output: String

            Given("a build script with default configuration") {
                temporaryFolder.createBuildScriptWithConfigFile()
            }

            And("config file with html only") {
                temporaryFolder.createPluginConfigurationWithHtmlReportOnly()
            }

            When("we execute the markdownlint task") {
                output = build(temporaryFolder, "markdownlint", "-q", "--stacktrace").output.trimEnd()
            }

            Then("no xml report generated") {
                assertThat(output).doesNotContain(
                    "Successfully generated Checkstyle XML report",
                    "build/reports/markdownlint/markdownlint.xml"
                )
            }

            And("html report generated") {
                assertThat(output).contains(
                    "Successfully generated HTML report",
                    "build/reports/markdownlint/markdownlint.html"
                )
            }
        }

        Scenario("xml only report config generates only xml report") {
            lateinit var output: String

            Given("a build script with default configuration") {
                temporaryFolder.createBuildScriptWithConfigFile()
            }

            And("config file with xml only") {
                temporaryFolder.createPluginConfigurationWithXmlReportOnly()
            }

            When("we execute the markdownlint task") {
                output = build(temporaryFolder, "markdownlint", "-q", "--stacktrace").output.trimEnd()
            }

            Then("xml report generated") {
                assertThat(output).contains(
                    "Successfully generated Checkstyle XML report",
                    "build/reports/markdownlint/markdownlint.xml"
                )
            }

            And("no html report generated") {
                assertThat(output).doesNotContain(
                    "Successfully generated HTML report",
                    "build/reports/markdownlint/markdownlint.html"
                )
            }
        }

        Scenario("invalid configuration throws an exception") {
            lateinit var output: String

            Given("a build script with default configuration") {
                temporaryFolder.createBuildScriptWithConfigFile()
            }

            And("invalid configuration") {
                temporaryFolder.createPluginConfigurationWithNoConfiguration()
            }

            When("we execute the markdownlint task") {
                output = buildAndFail(temporaryFolder, "markdownlint", "-q", "--stacktrace").output.trimEnd()
            }

            Then("build fails with invalid configuration") {
                assertThat(output).contains("Invalid configuration of markdownlint")
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

            When("we execute the markdownlint task") {
                output = build(temporaryFolder, "markdownlint", "-q", "--stacktrace").output.trimEnd()
            }

            Then("one error is reported") {
                val errors = Regex("SingleH1Rule").findAll(output).toList()
                assertThat(errors.size).isOne()
            }
            
            And("xml contains one error") {
            	val xmlReport = File(temporaryFolder.root, "build/reports/markdownlint/markdownlint.xml").readText().trim()
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

            When("we execute the markdownlint task") {
                output = build(temporaryFolder, "markdownlint", "-q", "--stacktrace").output.trimEnd()
            }

            Then("one error is reported") {
                val errors = Regex("SingleH1Rule").findAll(output).toList()
                assertThat(errors.size).isOne()
            }
            
            And("xml contains one error") {
            	val xmlReport = File(temporaryFolder.root, "build/reports/markdownlint/markdownlint.xml").readText().trim()
            	val errors = Regex("<error").findAll(xmlReport).toList()
            	assertThat(errors.size).isOne()
            }
        }
        
        Scenario("configuration changes errors reported") {
            lateinit var output: String

            Given("a build script with config file") {
                temporaryFolder.createBuildScriptWithConfigFile()
            }

            And("a config file disabling rule") {
                temporaryFolder.createPluginConfigurationDisablingRule()
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
            	val xmlReport = File(temporaryFolder.root, "build/reports/markdownlint/markdownlint.xml").readText().trim()
            	val errors = Regex("<error").findAll(xmlReport).toList()
            	assertThat(errors.size).isZero()
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

private fun TemporaryFolder.createBuildScriptWithConfigFile() = createFile("build.gradle.kts") {
    """
    plugins {
        id("com.appmattus.markdown")
    }
    markdownlint {
        configFile = File(projectDir, "markdownlint.kts")
    }
    """.trimIndent()
}

private fun TemporaryFolder.createPluginConfigurationWithEmptyConfiguration() = createFile("markdownlint.kts") {
    """
    markdownlint {

    }
    """.trimIndent()
}

private fun TemporaryFolder.createPluginConfigurationWithNoConfiguration() = createFile("markdownlint.kts") {
    ""
}

private fun TemporaryFolder.createPluginConfigurationDisablingRule() = createFile("markdownlint.kts") {
    """
    import com.appmattus.markdown.dsl.markdownlint
    import com.appmattus.markdown.rules.SingleH1Rule

    markdownlint {
        rules {
            +SingleH1Rule {
                active = false
            }
        }
    }
    """.trimIndent()
}

private fun TemporaryFolder.createPluginConfigurationWithXmlReportOnly() = createFile("markdownlint.kts") {
    """
    import com.appmattus.markdown.dsl.markdownlint
    import com.appmattus.markdown.rules.FirstHeaderH1Rule

    markdownlint {
        reports {
            checkstyle()
        }
    }
    """.trimIndent()
}

private fun TemporaryFolder.createPluginConfigurationWithHtmlReportOnly() = createFile("markdownlint.kts") {
    """
    import com.appmattus.markdown.dsl.markdownlint
    import com.appmattus.markdown.rules.FirstHeaderH1Rule

    markdownlint {
        reports {
            html()
        }
    }
    """.trimIndent()
}

private fun TemporaryFolder.createPluginConfigurationWithNoReports() = createFile("markdownlint.kts") {
    """
    import com.appmattus.markdown.dsl.markdownlint
    import com.appmattus.markdown.rules.FirstHeaderH1Rule

    markdownlint {
        reports {
        }
    }
    """.trimIndent()
}


private fun TemporaryFolder.createMarkdownFileWithNoErrors(filename: String) = createFile(filename) {
    """
    # Welcome to my project

    This is the introduction

    ## Section 2

    This is the next section
    """.trimIndent()
}

private fun TemporaryFolder.createMarkdownFileWithAnError(filename: String) = createFile(filename) {
    """
    # Welcome to my project

    This is the introduction

    # Section 2

    This is the next section
    """.trimIndent()
}

private fun build(temporaryFolder: TemporaryFolder, vararg arguments: String): BuildResult =
    GradleRunner
        .create()
        .withProjectDir(temporaryFolder.root)
        .withPluginClasspath()
        .withArguments(*arguments)
        .build()

private fun buildAndFail(temporaryFolder: TemporaryFolder, vararg arguments: String): BuildResult =
    GradleRunner
        .create()
        .withProjectDir(temporaryFolder.root)
        .withPluginClasspath()
        .withArguments(*arguments)
        .buildAndFail()

private fun TemporaryFolder.createFile(filename: String, content: () -> String): File {
    return newFile(filename).apply {
        writeText(content())
    }
}

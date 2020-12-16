package com.appmattus.markdown.processing

import com.appmattus.markdown.dsl.Config
import com.appmattus.markdown.plugin.MarkdownLint
import org.assertj.core.api.Assertions.assertThat
import org.junit.rules.TemporaryFolder
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import java.nio.file.Files

object RuleProcessorTest : Spek({
    fun RuleProcessor.processAndReturnOutput(config: Config): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        process(config, PrintStream(byteArrayOutputStream))
        return byteArrayOutputStream.toByteArray().toString(Charsets.UTF_8)
    }

    Feature("RuleProcessor") {
        val temporaryFolder by memoized {
            TemporaryFolder().apply {
                create()
            }
        }

        val rootDir by memoized {
            temporaryFolder.newFolder("rootDir").toPath()
        }

        val reportsDir by memoized {
            temporaryFolder.newFolder("reportsDir").toPath()
        }

        val slash = Regex.escape(File.separator)
        val htmlReportPattern = "reportsDir${slash}markdownlint\\.html".toRegex().toPattern()
        val xmlReportPattern = "reportsDir${slash}markdownlint\\.xml".toRegex().toPattern()

        Scenario("default config executes successfully") {
            lateinit var config: Config
            lateinit var ruleProcessor: RuleProcessor

            Given("a default config") {
                config = MarkdownLint().build()
            }

            When("we create the rule processor") {
                ruleProcessor = RuleProcessor(rootDir, reportsDir)
            }

            Then("processing the config file is successful") {
                ruleProcessor.process(config)
            }
        }

        Scenario("default config generates both xml and html reports") {
            lateinit var config: Config
            lateinit var output: String

            Given("a default config") {
                config = MarkdownLint().build()
            }

            When("we execute the rule processor") {
                output = RuleProcessor(rootDir, reportsDir).processAndReturnOutput(config)
            }

            Then("xml report generated") {
                assertThat(output).contains("Successfully generated Checkstyle XML report")
                assertThat(output).containsPattern(xmlReportPattern)
                assertThat(reportsDir.resolve("markdownlint.xml")).exists()
            }

            And("html report generated") {
                assertThat(output).contains("Successfully generated HTML report")
                assertThat(output).containsPattern(htmlReportPattern)
                assertThat(reportsDir.resolve("markdownlint.html")).exists()
            }
        }

        Scenario("empty report config generates no reports") {
            lateinit var config: Config
            lateinit var output: String

            Given("config disabling reports") {
                config = MarkdownLint().reports { }.build()
            }

            When("we execute the rule processor") {
                output = RuleProcessor(rootDir, reportsDir).processAndReturnOutput(config)
            }

            Then("no xml report generated") {
                assertThat(output).doesNotContain("Successfully generated Checkstyle XML report")
                assertThat(output).doesNotContainPattern(xmlReportPattern)
                assertThat(reportsDir.resolve("markdownlint.xml")).doesNotExist()
            }

            And("no html report generated") {
                assertThat(output).doesNotContain("Successfully generated HTML report")
                assertThat(output).doesNotContainPattern(htmlReportPattern)
                assertThat(reportsDir.resolve("markdownlint.html")).doesNotExist()
            }
        }

        Scenario("html only report config generates only html report") {
            lateinit var config: Config
            lateinit var output: String

            Given("config with html only") {
                config = MarkdownLint().reports { html() }.build()
            }

            When("we execute the rule processor") {
                output = RuleProcessor(rootDir, reportsDir).processAndReturnOutput(config)
            }

            Then("html report generated") {
                assertThat(output).contains("Successfully generated HTML report")
                assertThat(output).containsPattern(htmlReportPattern)
                assertThat(reportsDir.resolve("markdownlint.html")).exists()
            }

            And("no xml report generated") {
                assertThat(output).doesNotContain("Successfully generated Checkstyle XML report")
                assertThat(output).doesNotContainPattern(xmlReportPattern)
                assertThat(reportsDir.resolve("markdownlint.xml")).doesNotExist()
            }
        }

        Scenario("xml only report config generates only xml report") {
            lateinit var config: Config
            lateinit var output: String

            Given("config with xml only") {
                config = MarkdownLint().reports { checkstyle() }.build()
            }

            When("we execute the rule processor") {
                output = RuleProcessor(rootDir, reportsDir).processAndReturnOutput(config)
            }

            Then("xml report generated") {
                assertThat(output).contains("Successfully generated Checkstyle XML report")
                assertThat(output).containsPattern(xmlReportPattern)
                assertThat(reportsDir.resolve("markdownlint.xml")).exists()
            }

            And("no html report generated") {
                assertThat(output).doesNotContain("Successfully generated HTML report")
                assertThat(output).doesNotContainPattern(htmlReportPattern)
                assertThat(reportsDir.resolve("markdownlint.html")).doesNotExist()
            }
        }

        Scenario("no files analysed when no markdown files defined") {
            lateinit var config: Config
            lateinit var output: String

            Given("no markdown files") {
            }

            And("a default config") {
                config = MarkdownLint().build()
            }

            When("we execute the rule processor") {
                output = RuleProcessor(rootDir, reportsDir).processAndReturnOutput(config)
            }

            Then("no files analysed") {
                assertThat(output).contains("0 markdown files were analysed")
            }
        }

        Scenario("one file analysed when one markdown file defined") {
            lateinit var config: Config
            lateinit var output: String

            Given("one markdown files") {
                Files.write(rootDir.resolve("a-valid-file.md"), listOf("# A valid file"))
            }

            And("a default config") {
                config = MarkdownLint().build()
            }

            When("we execute the rule processor") {
                output = RuleProcessor(rootDir, reportsDir).processAndReturnOutput(config)
            }

            Then("one file analysed") {
                assertThat(output).contains("1 markdown files were analysed")
            }
        }

        Scenario("two files analysed when two markdown files defined") {
            lateinit var config: Config
            lateinit var output: String

            Given("two markdown files") {
                Files.write(rootDir.resolve("a-valid-file.md"), listOf("# A valid file"))
                Files.write(rootDir.resolve("another-valid-file.md"), listOf("# Another valid file"))
            }

            And("a default config") {
                config = MarkdownLint().build()
            }

            When("we execute the rule processor") {
                output = RuleProcessor(rootDir, reportsDir).processAndReturnOutput(config)
            }

            Then("two file analysed") {
                assertThat(output).contains("2 markdown files were analysed")
            }
        }

        Scenario("one file analysed when one file excluded") {
            lateinit var config: Config
            lateinit var output: String

            Given("two markdown files") {
                Files.write(rootDir.resolve("a-valid-file.md"), listOf("# A valid file"))
                Files.write(rootDir.resolve("an-invalid-file.md"), listOf(""))
            }

            And("config with exclude rule") {
                config = MarkdownLint().apply {
                    excludes = listOf(".*/an-invalid-file.md")
                }.build()
            }

            When("we execute the rule processor") {
                output = RuleProcessor(rootDir, reportsDir).processAndReturnOutput(config)
            }

            Then("one file analysed") {
                assertThat(output).contains("1 markdown files were analysed")
            }
        }

        Scenario("one file analysed when one file included") {
            lateinit var config: Config
            lateinit var output: String

            Given("two markdown files") {
                Files.write(rootDir.resolve("a-valid-file.md"), listOf("# A valid file"))
                Files.write(rootDir.resolve("an-invalid-file.md"), listOf(""))
            }

            And("config with exclude rule") {
                config = MarkdownLint().apply {
                    includes = listOf(".*/a-valid-file.md")
                }.build()
            }

            When("we execute the rule processor") {
                output = RuleProcessor(rootDir, reportsDir).processAndReturnOutput(config)
            }

            Then("one file analysed") {
                assertThat(output).contains("1 markdown files were analysed")
            }

            And("no errors were reported") {
                assertThat(output).contains("No errors reported")
            }
        }

        Scenario("no files analysed when a file is both included and excluded") {
            lateinit var config: Config
            lateinit var output: String

            Given("two markdown files") {
                Files.write(rootDir.resolve("a-valid-file.md"), listOf("# A valid file"))
                Files.write(rootDir.resolve("an-invalid-file.md"), listOf(""))
            }

            And("config with exclude rule") {
                config = MarkdownLint().apply {
                    includes = listOf(".*/a-valid-file.md")
                    excludes = listOf(".*/a-valid-file.md")
                }.build()
            }

            When("we execute the rule processor") {
                output = RuleProcessor(rootDir, reportsDir).processAndReturnOutput(config)
            }

            Then("no files analysed") {
                assertThat(output).contains("0 markdown files were analysed")
            }

            And("no errors were reported") {
                assertThat(output).contains("No errors reported")
            }
        }

        Scenario("one file analysed when directory excluded") {
            lateinit var config: Config
            lateinit var output: String

            Given("two markdown files") {
                Files.write(rootDir.resolve("a-valid-file.md"), listOf("# A valid file"))
                Files.createDirectory(rootDir.resolve("directory"))
                Files.write(rootDir.resolve("directory/an-invalid-file.md"), listOf(""))
            }

            And("config with exclude rule") {
                config = MarkdownLint().apply {
                    excludes = listOf(".*/directory/.*")
                }.build()
            }

            When("we execute the rule processor") {
                output = RuleProcessor(rootDir, reportsDir).processAndReturnOutput(config)
            }

            Then("one file analysed") {
                assertThat(output).contains("1 markdown files were analysed")
            }

            And("no errors were reported") {
                assertThat(output).contains("No errors reported")
            }
        }
    }
})

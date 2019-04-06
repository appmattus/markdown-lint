package com.appmattus.markdown.processing

import org.assertj.core.api.Assertions.assertThat
import org.junit.rules.TemporaryFolder
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import kotlin.test.assertFailsWith

object RuleProcessorTest : Spek({
    fun RuleProcessor.processAndReturnOutput(configFile: File?): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        process(configFile, PrintStream(byteArrayOutputStream))
        return byteArrayOutputStream.toByteArray().toString(Charsets.UTF_8)
    }

    Feature("RuleProcessor") {
        val temporaryFolder by memoized {
            TemporaryFolder().apply {
                create()
            }
        }

        val rootDir by memoized {
            temporaryFolder.newFolder("rootDir")
        }

        val reportsDir by memoized {
            temporaryFolder.newFolder("reportsDir")
        }

        val slash = Regex.escape(File.separator)
        val htmlReportPattern = "reportsDir${slash}markdownlint\\.html".toRegex().toPattern()
        val xmlReportPattern = "reportsDir${slash}markdownlint\\.xml".toRegex().toPattern()

        Scenario("invalid config file throws an exception") {
            lateinit var configFile: File
            lateinit var ruleProcessor: RuleProcessor

            Given("an invalid config file") {
                configFile = temporaryFolder.newFile("markdownConfig.gradle.kts")
            }

            When("we create the rule processor") {
                ruleProcessor = RuleProcessor(rootDir, reportsDir)
            }

            Then("processing the config file throws an exception") {
                assertFailsWith<IllegalStateException> {
                    ruleProcessor.process(configFile)
                }
            }

        }

        Scenario("default config file executes successfully") {
            var configFile: File? = null
            lateinit var ruleProcessor: RuleProcessor

            Given("a null config file") {
                configFile = null
            }

            When("we create the rule processor") {
                ruleProcessor = RuleProcessor(rootDir, reportsDir)
            }

            Then("processing the config file is successful") {
                ruleProcessor.process(configFile)
            }
        }

        Scenario("valid config file executes successfully") {
            lateinit var configFile: File
            lateinit var ruleProcessor: RuleProcessor

            Given("a valid config file") {
                configFile = temporaryFolder.newFile("markdownConfig.gradle.kts").apply {
                    writeText(
                        """
                            import com.appmattus.markdown.dsl.markdownLintConfig

                            markdownLintConfig {}
                        """.trimIndent()
                    )
                }
            }

            When("we create the rule processor") {
                ruleProcessor = RuleProcessor(rootDir, reportsDir)
            }

            Then("processing the config file is successful") {
                ruleProcessor.process(configFile)
            }
        }

        Scenario("default config generates both xml and html reports") {
            var configFile: File? = null
            lateinit var output: String

            Given("a null config file") {
                configFile = null
            }

            When("we execute the rule processor") {
                output = RuleProcessor(rootDir, reportsDir).processAndReturnOutput(configFile)
            }

            Then("xml report generated") {
                assertThat(output).contains("Successfully generated Checkstyle XML report")
                assertThat(output).containsPattern(xmlReportPattern)
                assertThat(File(reportsDir, "markdownlint.xml")).exists()
            }

            And("html report generated") {
                assertThat(output).contains("Successfully generated HTML report")
                assertThat(output).containsPattern(htmlReportPattern)
                assertThat(File(reportsDir, "markdownlint.html")).exists()
            }
        }

        Scenario("empty report config generates no reports") {
            lateinit var configFile: File
            lateinit var output: String

            Given("config file disabling reports") {
                configFile = temporaryFolder.newFile("markdownConfig.gradle.kts").apply {
                    writeText(
                        """
                            import com.appmattus.markdown.dsl.markdownLintConfig

                            markdownLintConfig {
                                reports {
                                }
                            }
                        """.trimIndent()
                    )
                }
            }

            When("we execute the rule processor") {
                output = RuleProcessor(rootDir, reportsDir).processAndReturnOutput(configFile)
            }

            Then("no xml report generated") {
                assertThat(output).doesNotContain("Successfully generated Checkstyle XML report")
                assertThat(output).doesNotContainPattern(xmlReportPattern)
                assertThat(File(reportsDir, "markdownlint.xml")).doesNotExist()
            }

            And("no html report generated") {
                assertThat(output).doesNotContain("Successfully generated HTML report")
                assertThat(output).doesNotContainPattern(htmlReportPattern)
                assertThat(File(reportsDir, "markdownlint.html")).doesNotExist()
            }
        }

        Scenario("html only report config generates only html report") {
            lateinit var configFile: File
            lateinit var output: String

            Given("config file with html only") {
                configFile = temporaryFolder.newFile("markdownConfig.gradle.kts").apply {
                    writeText(
                        """
                            import com.appmattus.markdown.dsl.markdownLintConfig

                            markdownLintConfig {
                                reports {
                                    html()
                                }
                            }
                        """.trimIndent()
                    )
                }
            }

            When("we execute the rule processor") {
                output = RuleProcessor(rootDir, reportsDir).processAndReturnOutput(configFile)
            }

            Then("html report generated") {
                assertThat(output).contains("Successfully generated HTML report")
                assertThat(output).containsPattern(htmlReportPattern)
                assertThat(File(reportsDir, "markdownlint.html")).exists()
            }

            And("no xml report generated") {
                assertThat(output).doesNotContain("Successfully generated Checkstyle XML report")
                assertThat(output).doesNotContainPattern(xmlReportPattern)
                assertThat(File(reportsDir, "markdownlint.xml")).doesNotExist()
            }
        }

        Scenario("xml only report config generates only xml report") {
            lateinit var configFile: File
            lateinit var output: String

            Given("config file with xml only") {
                configFile = temporaryFolder.newFile("markdownConfig.gradle.kts").apply {
                    writeText(
                        """
                            import com.appmattus.markdown.dsl.markdownLintConfig

                            markdownLintConfig {
                                reports {
                                    checkstyle()
                                }
                            }
                        """.trimIndent()
                    )
                }
            }

            When("we execute the rule processor") {
                output = RuleProcessor(rootDir, reportsDir).processAndReturnOutput(configFile)
            }

            Then("xml report generated") {
                assertThat(output).contains("Successfully generated Checkstyle XML report")
                assertThat(output).containsPattern(xmlReportPattern)
                assertThat(File(reportsDir, "markdownlint.xml")).exists()
            }

            And("no html report generated") {
                assertThat(output).doesNotContain("Successfully generated HTML report")
                assertThat(output).doesNotContainPattern(htmlReportPattern)
                assertThat(File(reportsDir, "markdownlint.html")).doesNotExist()
            }
        }


        Scenario("no files analysed when no markdown files defined") {
            lateinit var output: String

            Given("no markdown files") {

            }

            When("we execute the rule processor") {
                output = RuleProcessor(rootDir, reportsDir).processAndReturnOutput(null)
            }

            Then("no files analysed") {
                assertThat(output).contains("0 markdown files were analysed")
            }
        }

        Scenario("one file analysed when one markdown file defined") {
            lateinit var output: String

            Given("one markdown files") {
                File(rootDir, "a-valid-file.md").writeText("# A valid file")
            }

            When("we execute the rule processor") {
                output = RuleProcessor(rootDir, reportsDir).processAndReturnOutput(null)
            }

            Then("one file analysed") {
                assertThat(output).contains("1 markdown files were analysed")
            }
        }

        Scenario("two files analysed when two markdown files defined") {
            lateinit var output: String

            Given("two markdown files") {
                File(rootDir, "a-valid-file.md").writeText("# A valid file")
                File(rootDir, "another-valid-file.md").writeText("# Another valid file")
            }

            When("we execute the rule processor") {
                output = RuleProcessor(rootDir, reportsDir).processAndReturnOutput(null)
            }

            Then("two file analysed") {
                assertThat(output).contains("2 markdown files were analysed")
            }
        }

        Scenario("one file analysed when one file excluded") {
            lateinit var configFile: File
            lateinit var output: String

            Given("two markdown files") {
                File(rootDir, "a-valid-file.md").writeText("# A valid file")
                File(rootDir, "an-invalid-file.md").writeText("")
            }

            And("config file with exclude rule") {
                configFile = temporaryFolder.newFile("markdownConfig.gradle.kts").apply {
                    writeText(
                        """
                            import com.appmattus.markdown.dsl.markdownLintConfig

                            markdownLintConfig {
                                excludes(listOf(".*/an-invalid-file.md"))
                            }
                        """.trimIndent()
                    )
                }
            }

            When("we execute the rule processor") {
                output = RuleProcessor(rootDir, reportsDir).processAndReturnOutput(configFile)
            }

            Then("one file analysed") {
                assertThat(output).contains("1 markdown files were analysed")
            }
        }

        Scenario("one file analysed when one file included") {
            lateinit var configFile: File
            lateinit var output: String

            Given("two markdown files") {
                File(rootDir, "a-valid-file.md").writeText("# A valid file")
                File(rootDir, "an-invalid-file.md").writeText("")
            }

            And("config file with exclude rule") {
                configFile = temporaryFolder.newFile("markdownConfig.gradle.kts").apply {
                    writeText(
                        """
                            import com.appmattus.markdown.dsl.markdownLintConfig

                            markdownLintConfig {
                                includes(listOf(".*/a-valid-file.md"))
                            }
                        """.trimIndent()
                    )
                }
            }

            When("we execute the rule processor") {
                output = RuleProcessor(rootDir, reportsDir).processAndReturnOutput(configFile)
            }

            Then("one file analysed") {
                assertThat(output).contains("1 markdown files were analysed")
            }

            And("no errors were reported") {
                assertThat(output).contains("No errors reported")
            }
        }

        Scenario("no files analysed when a file is both included and excluded") {
            lateinit var configFile: File
            lateinit var output: String

            Given("two markdown files") {
                File(rootDir, "a-valid-file.md").writeText("# A valid file")
                File(rootDir, "an-invalid-file.md").writeText("")
            }

            And("config file with exclude rule") {
                configFile = temporaryFolder.newFile("markdownConfig.gradle.kts").apply {
                    writeText(
                        """
                            import com.appmattus.markdown.dsl.markdownLintConfig

                            markdownLintConfig {
                                includes(listOf(".*/a-valid-file.md"))
                                excludes(listOf(".*/a-valid-file.md"))
                            }
                        """.trimIndent()
                    )
                }
            }

            When("we execute the rule processor") {
                output = RuleProcessor(rootDir, reportsDir).processAndReturnOutput(configFile)
            }

            Then("no files analysed") {
                assertThat(output).contains("0 markdown files were analysed")
            }

            And("no errors were reported") {
                assertThat(output).contains("No errors reported")
            }
        }

        Scenario("one file analysed when directory excluded") {
            lateinit var configFile: File
            lateinit var output: String

            Given("two markdown files") {
                File(rootDir, "a-valid-file.md").writeText("# A valid file")
                File(rootDir, "directory").mkdir()
                File(rootDir, "directory/an-invalid-file.md").writeText("")
            }

            And("config file with exclude rule") {
                configFile = temporaryFolder.newFile("markdownConfig.gradle.kts").apply {
                    writeText(
                        """
                            import com.appmattus.markdown.dsl.markdownLintConfig

                            markdownLintConfig {
                                excludes(listOf(".*/directory/.*"))
                            }
                        """.trimIndent()
                    )
                }
            }

            When("we execute the rule processor") {
                output = RuleProcessor(rootDir, reportsDir).processAndReturnOutput(configFile)
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

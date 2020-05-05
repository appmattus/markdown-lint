package com.appmattus.markdown.processing

import com.appmattus.gherkin.dsl.And
import com.appmattus.gherkin.dsl.Given
import com.appmattus.gherkin.dsl.Then
import com.appmattus.gherkin.dsl.When
import com.appmattus.gherkin.jupiter.gherkin
import com.appmattus.markdown.dsl.Config
import com.appmattus.markdown.plugin.MarkdownLint
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.TestFactory
import org.junit.rules.TemporaryFolder
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream

class RuleProcessorTest {
    private fun RuleProcessor.processAndReturnOutput(config: Config): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        process(config, PrintStream(byteArrayOutputStream))
        return byteArrayOutputStream.toByteArray().toString(Charsets.UTF_8)
    }

    private val temporaryFolder = TemporaryFolder().apply {
        create()
    }

    private val rootDir = temporaryFolder.newFolder("rootDir")

    private val reportsDir = temporaryFolder.newFolder("reportsDir")

    private val slash = Regex.escape(File.separator)
    private val htmlReportPattern = "reportsDir${slash}markdownlint\\.html".toRegex().toPattern()
    private val xmlReportPattern = "reportsDir${slash}markdownlint\\.xml".toRegex().toPattern()

    @TestFactory
    fun `default config executes successfully`() = gherkin {
        val config: Config
        val ruleProcessor: RuleProcessor

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

    @TestFactory
    fun `default config generates both xml and html reports`() = gherkin {
        val config: Config
        val output: String

        Given("a default config") {
            config = MarkdownLint().build()
        }

        When("we execute the rule processor") {
            output = RuleProcessor(rootDir, reportsDir).processAndReturnOutput(config)
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

    @TestFactory
    fun `empty report config generates no reports`() = gherkin {
        val config: Config
        val output: String

        Given("config disabling reports") {
            config = MarkdownLint().reports { }.build()
        }

        When("we execute the rule processor") {
            output = RuleProcessor(rootDir, reportsDir).processAndReturnOutput(config)
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

    @TestFactory
    fun `html only report config generates only html report`() = gherkin {
        val config: Config
        val output: String

        Given("config with html only") {
            config = MarkdownLint().reports { html() }.build()
        }

        When("we execute the rule processor") {
            output = RuleProcessor(rootDir, reportsDir).processAndReturnOutput(config)
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

    @TestFactory
    fun `xml only report config generates only xml report`() = gherkin {
        val config: Config
        val output: String

        Given("config with xml only") {
            config = MarkdownLint().reports { checkstyle() }.build()
        }

        When("we execute the rule processor") {
            output = RuleProcessor(rootDir, reportsDir).processAndReturnOutput(config)
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

    @TestFactory
    fun `no files analysed when no markdown files defined`() = gherkin {
        val config: Config
        val output: String

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

    @TestFactory
    fun `one file analysed when one markdown file defined`() = gherkin {
        val config: Config
        val output: String

        Given("one markdown files") {
            File(rootDir, "a-valid-file.md").writeText("# A valid file\n")
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

    @TestFactory
    fun `two files analysed when two markdown files defined`() = gherkin {
        val config: Config
        val output: String

        Given("two markdown files") {
            File(rootDir, "a-valid-file.md").writeText("# A valid file\n")
            File(rootDir, "another-valid-file.md").writeText("# Another valid file\n")
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

    @TestFactory
    fun `one file analysed when one file excluded`() = gherkin {
        val config: Config
        val output: String

        Given("two markdown files") {
            File(rootDir, "a-valid-file.md").writeText("# A valid file\n")
            File(rootDir, "an-invalid-file.md").writeText("")
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

    @TestFactory
    fun `one file analysed when one file included`() = gherkin {
        val config: Config
        val output: String

        Given("two markdown files") {
            File(rootDir, "a-valid-file.md").writeText("# A valid file\n")
            File(rootDir, "an-invalid-file.md").writeText("")
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

    @TestFactory
    fun `no files analysed when a file is both included and excluded`() = gherkin {
        val config: Config
        val output: String

        Given("two markdown files") {
            File(rootDir, "a-valid-file.md").writeText("# A valid file\n")
            File(rootDir, "an-invalid-file.md").writeText("")
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

    @TestFactory
    fun `one file analysed when directory excluded`() = gherkin {
        val config: Config
        val output: String

        Given("two markdown files") {
            File(rootDir, "a-valid-file.md").writeText("# A valid file\n")
            File(rootDir, "directory").mkdir()
            File(rootDir, "directory/an-invalid-file.md").writeText("")
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

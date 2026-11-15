package com.appmattus.markdown.processing

import com.appmattus.markdown.dsl.Config
import com.appmattus.markdown.dsl.Report
import com.google.common.jimfs.Jimfs
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import java.nio.file.Files
import kotlin.test.Test

class RuleProcessorTest {
    private fun RuleProcessor.processAndReturnOutput(config: Config): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        process(config, PrintStream(byteArrayOutputStream))
        return byteArrayOutputStream.toByteArray().toString(Charsets.UTF_8)
    }

    // An in-memory file system proves the processor doesn't depend on the default file system
    private val fileSystem = Jimfs.newFileSystem()

    private val rootDir by lazy {
        Files.createDirectories(fileSystem.getPath("rootDir"))
    }

    private val reportsDir by lazy {
        Files.createDirectories(fileSystem.getPath("reportsDir"))
    }

    @After
    fun closeFileSystem() {
        fileSystem.close()
    }

    private val slash = Regex.escape(File.separator)
    private val htmlReportPattern = "reportsDir${slash}markdownlint\\.html".toRegex().toPattern()
    private val xmlReportPattern = "reportsDir${slash}markdownlint\\.xml".toRegex().toPattern()

    @Test
    fun `default config executes successfully`() {
        lateinit var config: Config
        lateinit var ruleProcessor: RuleProcessor

        // Given a default config
        config = Config()

        // When we create the rule processor
        ruleProcessor = RuleProcessor(rootDir, reportsDir)

        // Then processing the config file is successful
        ruleProcessor.process(config)
    }

    @Test
    fun `default config generates both xml and html reports`() {
        lateinit var config: Config
        lateinit var output: String

        // Given a default config
        config = Config()

        // When we execute the rule processor
        output = RuleProcessor(rootDir, reportsDir).processAndReturnOutput(config)

        // Then xml report generated
        assertThat(output).contains("Successfully generated Checkstyle XML report")
        assertThat(output).containsPattern(xmlReportPattern)
        assertThat(reportsDir.resolve("markdownlint.xml")).exists()

        // And html report generated
        assertThat(output).contains("Successfully generated HTML report")
        assertThat(output).containsPattern(htmlReportPattern)
        assertThat(reportsDir.resolve("markdownlint.html")).exists()
    }

    @Test
    fun `empty report config generates no reports`() {
        lateinit var config: Config
        lateinit var output: String

        // Given config disabling reports
        config = Config(reports = emptySet())

        // When we execute the rule processor
        output = RuleProcessor(rootDir, reportsDir).processAndReturnOutput(config)

        // Then no xml report generated
        assertThat(output).doesNotContain("Successfully generated Checkstyle XML report")
        assertThat(output).doesNotContainPattern(xmlReportPattern)
        assertThat(reportsDir.resolve("markdownlint.xml")).doesNotExist()

        // And no html report generated
        assertThat(output).doesNotContain("Successfully generated HTML report")
        assertThat(output).doesNotContainPattern(htmlReportPattern)
        assertThat(reportsDir.resolve("markdownlint.html")).doesNotExist()
    }

    @Test
    fun `html only report config generates only html report`() {
        lateinit var config: Config
        lateinit var output: String

        // Given config with html only
        config = Config(reports = setOf(Report.Html))

        // When we execute the rule processor
        output = RuleProcessor(rootDir, reportsDir).processAndReturnOutput(config)

        // Then html report generated
        assertThat(output).contains("Successfully generated HTML report")
        assertThat(output).containsPattern(htmlReportPattern)
        assertThat(reportsDir.resolve("markdownlint.html")).exists()

        // And no xml report generated
        assertThat(output).doesNotContain("Successfully generated Checkstyle XML report")
        assertThat(output).doesNotContainPattern(xmlReportPattern)
        assertThat(reportsDir.resolve("markdownlint.xml")).doesNotExist()
    }

    @Test
    fun `xml only report config generates only xml report`() {
        lateinit var config: Config
        lateinit var output: String

        // Given config with xml only
        config = Config(reports = setOf(Report.Checkstyle))

        // When we execute the rule processor
        output = RuleProcessor(rootDir, reportsDir).processAndReturnOutput(config)

        // Then xml report generated
        assertThat(output).contains("Successfully generated Checkstyle XML report")
        assertThat(output).containsPattern(xmlReportPattern)
        assertThat(reportsDir.resolve("markdownlint.xml")).exists()

        // And no html report generated
        assertThat(output).doesNotContain("Successfully generated HTML report")
        assertThat(output).doesNotContainPattern(htmlReportPattern)
        assertThat(reportsDir.resolve("markdownlint.html")).doesNotExist()
    }

    @Test
    fun `no files analysed when no markdown files defined`() {
        lateinit var config: Config
        lateinit var output: String

        // Given no markdown files

        // And a default config
        config = Config()

        // When we execute the rule processor
        output = RuleProcessor(rootDir, reportsDir).processAndReturnOutput(config)

        // Then no files analysed
        assertThat(output).contains("0 markdown files were analysed")
    }

    @Test
    fun `one file analysed when one markdown file defined`() {
        lateinit var config: Config
        lateinit var output: String

        // Given one markdown files
        Files.write(rootDir.resolve("a-valid-file.md"), listOf("# A valid file"))

        // And a default config
        config = Config()

        // When we execute the rule processor
        output = RuleProcessor(rootDir, reportsDir).processAndReturnOutput(config)

        // Then one file analysed
        assertThat(output).contains("1 markdown files were analysed")
    }

    @Test
    fun `two files analysed when two markdown files defined`() {
        lateinit var config: Config
        lateinit var output: String

        // Given two markdown files
        Files.write(rootDir.resolve("a-valid-file.md"), listOf("# A valid file"))
        Files.write(rootDir.resolve("another-valid-file.md"), listOf("# Another valid file"))

        // And a default config
        config = Config()

        // When we execute the rule processor
        output = RuleProcessor(rootDir, reportsDir).processAndReturnOutput(config)

        // Then two file analysed
        assertThat(output).contains("2 markdown files were analysed")
    }

    @Test
    fun `one file analysed when one file excluded`() {
        lateinit var config: Config
        lateinit var output: String

        // Given two markdown files
        Files.write(rootDir.resolve("a-valid-file.md"), listOf("# A valid file"))
        Files.write(rootDir.resolve("an-invalid-file.md"), listOf(""))

        // And config with exclude rule
        config = Config(
            excludes = listOf(".*/an-invalid-file.md")
        )

        // When we execute the rule processor
        output = RuleProcessor(rootDir, reportsDir).processAndReturnOutput(config)

        // Then one file analysed
        assertThat(output).contains("1 markdown files were analysed")
    }

    @Test
    fun `one file analysed when one file included`() {
        lateinit var config: Config
        lateinit var output: String

        // Given two markdown files
        Files.write(rootDir.resolve("a-valid-file.md"), listOf("# A valid file"))
        Files.write(rootDir.resolve("an-invalid-file.md"), listOf(""))

        // And config with exclude rule
        config = Config(
            includes = listOf(".*/a-valid-file.md")
        )

        // When we execute the rule processor
        output = RuleProcessor(rootDir, reportsDir).processAndReturnOutput(config)

        // Then one file analysed
        assertThat(output).contains("1 markdown files were analysed")

        // And no errors were reported
        assertThat(output).contains("No errors reported")
    }

    @Test
    fun `no files analysed when a file is both included and excluded`() {
        lateinit var config: Config
        lateinit var output: String

        // Given two markdown files
        Files.write(rootDir.resolve("a-valid-file.md"), listOf("# A valid file"))
        Files.write(rootDir.resolve("an-invalid-file.md"), listOf(""))

        // And config with exclude rule
        config = Config(
            includes = listOf(".*/a-valid-file.md"),
            excludes = listOf(".*/a-valid-file.md")
        )

        // When we execute the rule processor
        output = RuleProcessor(rootDir, reportsDir).processAndReturnOutput(config)

        // Then no files analysed
        assertThat(output).contains("0 markdown files were analysed")

        // And no errors were reported
        assertThat(output).contains("No errors reported")
    }

    @Test
    fun `one file analysed when directory excluded`() {
        lateinit var config: Config
        lateinit var output: String

        // Given two markdown files
        Files.write(rootDir.resolve("a-valid-file.md"), listOf("# A valid file"))
        Files.createDirectory(rootDir.resolve("directory"))
        Files.write(rootDir.resolve("directory/an-invalid-file.md"), listOf(""))

        // And config with exclude rule
        config = Config(
            excludes = listOf(".*/directory/.*")
        )

        // When we execute the rule processor
        output = RuleProcessor(rootDir, reportsDir).processAndReturnOutput(config)

        // Then one file analysed
        assertThat(output).contains("1 markdown files were analysed")

        // And no errors were reported
        assertThat(output).contains("No errors reported")
    }
}

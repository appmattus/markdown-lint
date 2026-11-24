package com.appmattus.markdown.readme

import com.appmattus.markdown.dsl.Config
import com.appmattus.markdown.dsl.RulesBuilder
import com.appmattus.markdown.processing.MarkdownLinter
import com.appmattus.markdown.processing.RuleProcessor
import com.appmattus.markdown.rules.SingleH1Rule
import com.google.common.jimfs.Jimfs
import org.assertj.core.api.Assertions.assertThat
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.nio.file.Files
import kotlin.test.Test

class ReadmeExampleTest {
    @Test
    fun `readme example for using the analyzer without gradle runs`() {
        Jimfs.newFileSystem().use { fileSystem ->
            // Given a markdown file that only breaks the rule the example disables
            val rootDir = Files.createDirectories(fileSystem.getPath("root"))
            Files.write(rootDir.resolve("readme.md"), listOf("# Title", "", "# Second title"))

            // And the configuration from the readme
            val config = Config(
                rules = RulesBuilder().apply {
                    +SingleH1Rule {
                        active = false
                    }
                }.build()
            )

            // When we run the processor as in the readme
            val output = ByteArrayOutputStream()
            RuleProcessor(rootDir = rootDir, reportsDir = rootDir.resolve("build/reports/markdownlint"))
                .process(config, PrintStream(output))

            // Then the file is analysed without errors
            assertThat(output.toString()).contains("1 markdown files were analysed")
            assertThat(output.toString()).contains("No errors reported")
        }
    }

    @Test
    fun `readme example for linting content runs`() {
        Jimfs.newFileSystem().use { fileSystem ->
            // Given the configuration from the readme, which disables SingleH1Rule
            val config = Config(
                rules = RulesBuilder().apply {
                    +SingleH1Rule {
                        active = false
                    }
                }.build()
            )
            val rootDir = fileSystem.getPath("root")

            // When we lint content that doesn't exist on disk, as in the readme
            val linter = MarkdownLinter(rootDir = rootDir, config = config)
            val errors = linter.lint(rootDir.resolve("docs/guide.md"), "# Title\n\n# Second title\n")

            // Then the disabled rule isn't reported
            assertThat(errors).isEmpty()
        }
    }
}

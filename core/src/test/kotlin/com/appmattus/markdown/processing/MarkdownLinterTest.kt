package com.appmattus.markdown.processing

import com.appmattus.markdown.dsl.Config
import com.appmattus.markdown.dsl.RulesBuilder
import com.appmattus.markdown.errors.Error
import com.appmattus.markdown.rules.LowerCaseFilenameRule
import com.appmattus.markdown.rules.NoTrailingSpacesRule
import com.appmattus.markdown.rules.SingleH1Rule
import com.google.common.jimfs.Jimfs
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.Test

class MarkdownLinterTest {

    // An in-memory file system shows the linter doesn't need files to exist on disk
    private val fileSystem = Jimfs.newFileSystem()

    private val rootDir: Path = fileSystem.getPath("root")

    private val twoTopLevelHeaders = "# Title\n\n# Second title\n"

    @After
    fun closeFileSystem() {
        fileSystem.close()
    }

    private fun List<Error>.ruleNames() = map { it.ruleClass.simpleName }

    @Test
    fun `default rules report errors with line and column`() {
        lateinit var errors: List<Error>

        // Given a linter with the default rules
        val linter = MarkdownLinter(rootDir)

        // When we lint content with a second top level header
        errors = linter.lint(rootDir.resolve("readme.md"), twoTopLevelHeaders)

        // Then the duplicate header is reported at its line and column
        val error = errors.single { it.ruleClass == SingleH1Rule::class.java }
        assertThat(error.lineNumber).isEqualTo(3)
        assertThat(error.columnNumber).isEqualTo(1)
    }

    @Test
    fun `content is linted instead of the file on disk`() {
        lateinit var errors: List<Error>

        // Given a file on disk without errors
        val file = Files.createDirectories(rootDir).resolve("readme.md")
        Files.write(file, listOf("# Title"))

        // When we lint different content for that file
        errors = MarkdownLinter(rootDir).lint(file, twoTopLevelHeaders)

        // Then the errors come from the content
        assertThat(errors.ruleNames()).contains("SingleH1Rule")
    }

    @Test
    fun `filename rules check the given path`() {
        lateinit var errors: List<Error>

        // Given a linter with a filename rule
        val linter = MarkdownLinter(rootDir, listOf(LowerCaseFilenameRule()))

        // When we lint content for an uppercase filename that doesn't exist
        errors = linter.lint(rootDir.resolve("Guide.md"), "# Title\n")

        // Then the filename is reported
        assertThat(errors.ruleNames()).containsExactly("LowerCaseFilenameRule")
    }

    @Test
    fun `rule filters are matched relative to the root`() {
        // Given a rule excluded from the docs directory
        val linter = MarkdownLinter(rootDir, listOf(SingleH1Rule { excludes = listOf(".*/docs/.*") }))

        // When we lint the same content inside and outside the docs directory
        val docsErrors = linter.lint(rootDir.resolve("docs/guide.md"), twoTopLevelHeaders)
        val rootErrors = linter.lint(rootDir.resolve("readme.md"), twoTopLevelHeaders)

        // Then only the file outside the docs directory is reported
        assertThat(docsErrors).isEmpty()
        assertThat(rootErrors.ruleNames()).containsExactly("SingleH1Rule")
    }

    @Test
    fun `only the given rules run`() {
        lateinit var errors: List<Error>

        // Given a linter with a single rule
        val linter = MarkdownLinter(rootDir, listOf(SingleH1Rule()))

        // When we lint content that also has trailing spaces
        errors = linter.lint(rootDir.resolve("readme.md"), "# Title \n\n# Second title\n")

        // Then only the given rule reports errors
        assertThat(errors.ruleNames()).containsExactly("SingleH1Rule")
    }

    @Test
    fun `inactive rules don't run`() {
        lateinit var errors: List<Error>

        // Given a linter whose only rule is inactive
        val linter = MarkdownLinter(rootDir, listOf(SingleH1Rule { active = false }))

        // When we lint content that breaks it
        errors = linter.lint(rootDir.resolve("readme.md"), twoTopLevelHeaders)

        // Then no errors are reported
        assertThat(errors).isEmpty()
    }

    @Test
    fun `config rules override the default rules`() {
        lateinit var errors: List<Error>

        // Given a config that disables one rule
        val config = Config(rules = RulesBuilder().apply { +SingleH1Rule { active = false } }.build())
        val linter = MarkdownLinter(rootDir, config)

        // When we lint content that breaks the disabled rule and another rule
        errors = linter.lint(rootDir.resolve("readme.md"), "# Title \n\n# Second title\n")

        // Then the other default rules still run
        assertThat(errors.ruleNames()).contains(NoTrailingSpacesRule::class.java.simpleName)

        // And the disabled rule doesn't
        assertThat(errors.ruleNames()).doesNotContain("SingleH1Rule")
    }
}

package com.appmattus.markdown.spotless

import com.diffplug.spotless.FormatterStep
import com.diffplug.spotless.Lint
import org.assertj.core.api.Assertions.assertThat
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import kotlin.test.Test

class MarkdownLintStepTest {

    // Files aren't read, so they don't need to exist
    private val readme = File("project", "readme.md")

    private val twoTopLevelHeaders = "# Title\n\n# Second title\n"

    private fun List<Lint>.forRule(name: String) = filter { it.shortCode == name }

    // Spotless declares lint as nullable
    private fun FormatterStep.lints(content: String, file: File): List<Lint> = lint(content, file).orEmpty()

    @Test
    fun `format leaves content unchanged`() {
        // Given the step
        val step = MarkdownLintStep.create()

        // When we format content with errors
        val formatted = step.format(twoTopLevelHeaders, readme)

        // Then the content is unchanged
        assertThat(formatted).isEqualTo(twoTopLevelHeaders)
    }

    @Test
    fun `lint reports errors with the rule name, line and column`() {
        // Given the step
        val step = MarkdownLintStep.create()

        // When we lint content with a second top level header
        val lints = step.lints(twoTopLevelHeaders, readme)

        // Then it is reported by rule name at its line
        val lint = lints.forRule("SingleH1Rule").single()
        assertThat(lint.lineStart).isEqualTo(3)
        assertThat(lint.lineEnd).isEqualTo(3)

        // And the column is part of the detail, as Spotless lints only have lines
        assertThat(lint.detail).endsWith("(column 1)")
    }

    @Test
    fun `lint reports nothing for content without errors`() {
        // Given the step
        val step = MarkdownLintStep.create()

        // When we lint content without errors
        val lints = step.lints("# Title\n\nSome text.\n", readme)

        // Then there are no lints
        assertThat(lints).isEmpty()
    }

    @Test
    fun `filename rules check the file being linted`() {
        // Given the step
        val step = MarkdownLintStep.create()

        // When we lint a file with an uppercase name
        val lints = step.lints("# Title\n", File("project", "Guide.md"))

        // Then the filename is reported
        assertThat(lints.forRule("LowerCaseFilenameRule")).hasSize(1)
    }

    @Test
    fun `errors spanning lines are reported as a line range`() {
        // Given the step
        val step = MarkdownLintStep.create()

        // When we lint blockquotes separated by a blank line
        val lints = step.lints("> First\n\n> Second\n", readme)

        // Then the error covers the lines from the first blockquote to the blank line
        val lint = lints.forRule("NoBlanksBlockquoteRule").single()
        assertThat(lint.lineStart).isEqualTo(1)
        assertThat(lint.lineEnd).isEqualTo(2)
    }

    @Test
    fun `steps are equal so Spotless can tell when files need checking again`() {
        // Given two steps
        val first = MarkdownLintStep.create()
        val second = MarkdownLintStep.create()

        // Then they're equal and named for lint suppression
        assertThat(first).isEqualTo(second)
        assertThat(first.hashCode()).isEqualTo(second.hashCode())
        assertThat(first.name).isEqualTo(MarkdownLintStep.NAME)
    }

    @Test
    fun `step survives serialization, as for the configuration cache`() {
        // Given a step
        val step = MarkdownLintStep.create()

        // When we serialize and deserialize it
        val bytes = ByteArrayOutputStream().also { ObjectOutputStream(it).use { out -> out.writeObject(step) } }
        val restored = ObjectInputStream(ByteArrayInputStream(bytes.toByteArray())).use { it.readObject() }
            as FormatterStep

        // Then it's equal to the original
        assertThat(restored).isEqualTo(step)

        // And it still lints
        assertThat(restored.lints(twoTopLevelHeaders, readme).forRule("SingleH1Rule")).hasSize(1)
    }
}

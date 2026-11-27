package com.appmattus.markdown.spotless

import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.processing.MarkdownDocument
import com.appmattus.markdown.rules.LineLengthRule
import com.appmattus.markdown.rules.Rule
import com.appmattus.markdown.rules.SingleH1Rule
import com.diffplug.spotless.FormatterStep
import com.diffplug.spotless.Lint
import org.assertj.core.api.Assertions.assertThat
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import kotlin.test.Test

class ConfiguredMarkdownLintStepTest {

    // Files aren't read, so they don't need to exist
    private val projectDir = File("project").absoluteFile
    private val readme = File(projectDir, "readme.md")
    private val guide = File(projectDir, "docs/guide.md")

    private val twoTopLevelHeaders = "# Title\n\n# Second title\n"

    private class NoTodoRule(config: RuleSetup.Builder.() -> Unit = {}) : Rule(config) {
        override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) {
            document.lines.filter { it.contains("TODO") }.forEach { line ->
                errorReporter.reportError(line.startOffset, line.endOffset, "Remove TODOs")
            }
        }
    }

    private fun FormatterStep.lints(content: String, file: File): List<Lint> = lint(content, file).orEmpty()

    private fun List<Lint>.forRule(name: String) = filter { it.shortCode == name }

    private fun FormatterStep.roundTrip(): FormatterStep {
        val bytes = ByteArrayOutputStream().also { ObjectOutputStream(it).use { out -> out.writeObject(this) } }
        return ObjectInputStream(ByteArrayInputStream(bytes.toByteArray())).use { it.readObject() } as FormatterStep
    }

    @Test
    fun `configured rule settings are used`() {
        // Given a step with a shorter line length
        val step = MarkdownLintStep.create(projectDir) {
            +LineLengthRule(lineLength = 20)
        }

        // When we lint a line longer than 20 but shorter than the default 80
        val lints = step.lints("# Title\n\nThis line is longer than twenty.\n", readme)

        // Then it is reported
        assertThat(lints.forRule("LineLengthRule")).hasSize(1)
    }

    @Test
    fun `default rules still run alongside configured ones`() {
        // Given a step configuring one rule
        val step = MarkdownLintStep.create(projectDir) {
            +LineLengthRule(lineLength = 120)
        }

        // When we lint content that breaks a default rule
        val lints = step.lints(twoTopLevelHeaders, readme)

        // Then the default rule reports it
        assertThat(lints.forRule("SingleH1Rule")).hasSize(1)
    }

    @Test
    fun `disabled rules don't run`() {
        // Given a step disabling a rule
        val step = MarkdownLintStep.create(projectDir) {
            +SingleH1Rule { active = false }
        }

        // When we lint content that breaks it
        val lints = step.lints(twoTopLevelHeaders, readme)

        // Then it isn't reported
        assertThat(lints.forRule("SingleH1Rule")).isEmpty()
    }

    @Test
    fun `rule filters are matched relative to the root`() {
        // Given a step excluding a rule from the docs directory
        val step = MarkdownLintStep.create(projectDir) {
            +SingleH1Rule { excludes = listOf(".*/docs/.*") }
        }

        // Then the rule only reports the file outside the docs directory
        assertThat(step.lints(twoTopLevelHeaders, guide).forRule("SingleH1Rule")).isEmpty()
        assertThat(step.lints(twoTopLevelHeaders, readme).forRule("SingleH1Rule")).hasSize(1)
    }

    @Test
    fun `custom rules run`() {
        // Given a step with a custom rule
        val step = MarkdownLintStep.create(projectDir) {
            +NoTodoRule()
        }

        // When we lint content with a TODO
        val lints = step.lints("# Title\n\nTODO write this\n", readme)

        // Then the custom rule reports it
        assertThat(lints.forRule("NoTodoRule").single().lineStart).isEqualTo(3)
    }

    @Test
    fun `steps with the same rules are equal in any root, so cached results move between checkouts`() {
        // Given the same rules in two different project directories
        val first = MarkdownLintStep.create(File("checkout-1").absoluteFile) { +LineLengthRule(lineLength = 120) }
        val second = MarkdownLintStep.create(File("checkout-2").absoluteFile) { +LineLengthRule(lineLength = 120) }

        // Then the steps are equal
        assertThat(first).isEqualTo(second)
        assertThat(first.hashCode()).isEqualTo(second.hashCode())
    }

    @Test
    fun `steps with different rule settings are not equal, so files are checked again`() {
        // Given different settings for a rule
        val first = MarkdownLintStep.create(projectDir) { +LineLengthRule(lineLength = 120) }
        val second = MarkdownLintStep.create(projectDir) { +LineLengthRule(lineLength = 100) }

        // Then the steps differ
        assertThat(first).isNotEqualTo(second)
    }

    @Test
    fun `rules list and rules block create equal steps`() {
        // Given the same rule passed both ways
        val fromList = MarkdownLintStep.create(projectDir, listOf(SingleH1Rule { active = false }))
        val fromBlock = MarkdownLintStep.create(projectDir) { +SingleH1Rule { active = false } }

        // Then the steps are equal
        assertThat(fromList).isEqualTo(fromBlock)
    }

    @Test
    fun `step survives serialization with its root and rules, as for the configuration cache`() {
        // Given a step with a filter relative to its root and a custom rule
        val step = MarkdownLintStep.create(projectDir) {
            +SingleH1Rule { excludes = listOf(".*/docs/.*") }
            +NoTodoRule()
        }

        // When we serialize and deserialize it
        val restored = step.roundTrip()

        // Then it's equal to the original
        assertThat(restored).isEqualTo(step)

        // And it still applies the filter relative to its root
        assertThat(restored.lints(twoTopLevelHeaders, guide).forRule("SingleH1Rule")).isEmpty()
        assertThat(restored.lints(twoTopLevelHeaders, readme).forRule("SingleH1Rule")).hasSize(1)

        // And it still runs the custom rule
        assertThat(restored.lints("# Title\n\nTODO\n", readme).forRule("NoTodoRule")).hasSize(1)
    }
}

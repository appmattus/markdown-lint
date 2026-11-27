package com.appmattus.markdown.spotless

import com.appmattus.markdown.dsl.Config
import com.appmattus.markdown.dsl.RulesBuilder
import com.appmattus.markdown.errors.Error
import com.appmattus.markdown.processing.MarkdownLinter
import com.appmattus.markdown.rules.AllRules
import com.appmattus.markdown.rules.Rule
import com.diffplug.spotless.FormatterFunc
import com.diffplug.spotless.FormatterStep
import com.diffplug.spotless.Lint
import com.diffplug.spotless.SerializedFunction
import java.io.File
import java.io.Serializable
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.ConcurrentHashMap

/**
 * A Spotless step that reports markdown-lint errors as lints:
 *
 * ```
 * spotless {
 *     format("markdown") {
 *         target("**&#47;*.md", "**&#47;*.markdown")
 *         addStep(MarkdownLintStep.create(projectDir) {
 *             +LineLengthRule(lineLength = 120)
 *         })
 *     }
 * }
 * ```
 *
 * The step never changes files. Spotless fails on the lints it reports until they're fixed, or suppressed with
 * `suppressLintsFor { step = MarkdownLintStep.NAME; shortCode = "SingleH1Rule" }`, where the short code is the rule's
 * class name.
 */
object MarkdownLintStep {

    /** The step's name, as used by Spotless to identify it, for example when suppressing its lints. */
    const val NAME = "markdownlint"

    /**
     * Uses the built-in rules with their default settings. The rules' `includes` and `excludes` match every file, so no
     * root directory is needed.
     */
    @JvmStatic
    fun create(): FormatterStep = FormatterStep.create(NAME, DefaultState(markdownLintVersion()), CreateDefaultFunc())

    /**
     * Uses the built-in rules, overridden by [rules] in the same way as the Gradle plugin's `rules` block. Rules can
     * change their settings, be disabled with `active = false`, or be custom rules.
     *
     * [rootDir], usually the project directory, is what each rule's `includes` and `excludes` patterns are matched
     * against.
     */
    @JvmStatic
    @JvmOverloads
    fun create(rootDir: File, rules: List<Rule> = emptyList()): FormatterStep {
        // Resolve each rule's setup now, as the setup lambdas aren't serialized with the rules
        val allRules = AllRules(Config(rules = rules)).rules.onEach { it.configuration }

        return FormatterStep.create(
            NAME,
            RulesState(rootDir.absolutePath, markdownLintVersion(), ArrayList(allRules)),
            ToEqualityState(),
            CreateRulesFunc()
        )
    }

    /**
     * Uses the built-in rules, overridden by the rules added in [rules], with the same syntax as the Gradle plugin's
     * `rules` block.
     */
    @JvmStatic
    fun create(rootDir: File, rules: RulesBuilder.() -> Unit): FormatterStep =
        create(rootDir, RulesBuilder().apply(rules).build())

    /**
     * Spotless only re-checks files when a step's serialized state changes. The markdown-lint version is part of the
     * state, so upgrading markdown-lint re-checks files against its rules.
     */
    private data class DefaultState(val version: String) : Serializable {
        private companion object {
            private const val serialVersionUID = 1L
        }
    }

    /** What Spotless stores to recreate the step, for example in the configuration cache. */
    private class RulesState(val rootDir: String, val version: String, val rules: ArrayList<Rule>) : Serializable {
        private companion object {
            private const val serialVersionUID = 1L
        }
    }

    /**
     * What Spotless compares to decide whether to check files again. Rule patterns match paths relative to the root,
     * and Spotless tracks files by their relative path, so the absolute root is transient. It's still available to lint
     * with, but isn't part of the comparison or cache key, which keeps them the same in every checkout.
     */
    private class EqualityState(
        @Transient val rootDir: String?,
        val version: String,
        val rules: ArrayList<Rule>
    ) : Serializable {
        private companion object {
            private const val serialVersionUID = 1L
        }
    }

    private class ToEqualityState : SerializedFunction<RulesState, EqualityState> {
        override fun apply(state: RulesState) = EqualityState(state.rootDir, state.version, state.rules)

        private companion object {
            private const val serialVersionUID = 1L
        }
    }

    private class CreateDefaultFunc : SerializedFunction<DefaultState, FormatterFunc> {
        override fun apply(state: DefaultState): FormatterFunc = DefaultRulesFormatterFunc()

        private companion object {
            private const val serialVersionUID = 1L
        }
    }

    private class CreateRulesFunc : SerializedFunction<EqualityState, FormatterFunc> {
        override fun apply(state: EqualityState): FormatterFunc {
            val rootDir = checkNotNull(state.rootDir) { "The markdownlint step's root directory wasn't restored" }
            return RulesFormatterFunc(MarkdownLinter(Paths.get(rootDir), state.rules))
        }

        private companion object {
            private const val serialVersionUID = 1L
        }
    }

    private fun markdownLintVersion() = MarkdownLinter::class.java.`package`?.implementationVersion ?: "unknown"
}

/** Lints with a linter chosen for each file; never changes the content. */
internal abstract class LintingFormatterFunc : FormatterFunc {

    protected abstract fun linterFor(path: Path): MarkdownLinter

    override fun apply(input: String): String = input

    override fun lint(content: String, file: File): List<Lint> {
        val path = file.toPath().toAbsolutePath()
        return linterFor(path).lint(path, content).map { it.toLint(content) }
    }

    private fun Error.toLint(content: String): Lint {
        val shortCode = ruleClass.simpleName
        val detail = "$errorMessage (column $columnNumber)"
        val endLine = lineNumberAt(content, maxOf(startOffset, endOffset - 1))

        return if (endLine > lineNumber) {
            Lint.atLineRange(lineNumber, endLine, shortCode, detail)
        } else {
            Lint.atLine(lineNumber, shortCode, detail)
        }
    }

    private fun lineNumberAt(content: String, offset: Int): Int {
        val end = offset.coerceIn(0, content.length)
        return 1 + (0 until end).count { content[it] == '\n' }
    }
}

internal class DefaultRulesFormatterFunc : LintingFormatterFunc() {

    // Every default rule includes all files and excludes none, so any root gives the same result; one per file system
    private val linters = ConcurrentHashMap<Path, MarkdownLinter>()

    override fun linterFor(path: Path): MarkdownLinter {
        val root = path.root ?: path
        return linters.getOrPut(root) { MarkdownLinter(root) }
    }
}

internal class RulesFormatterFunc(private val linter: MarkdownLinter) : LintingFormatterFunc() {
    override fun linterFor(path: Path) = linter
}

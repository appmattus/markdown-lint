package com.appmattus.markdown.spotless

import com.appmattus.markdown.errors.Error
import com.appmattus.markdown.processing.MarkdownLinter
import com.diffplug.spotless.FormatterFunc
import com.diffplug.spotless.FormatterStep
import com.diffplug.spotless.Lint
import com.diffplug.spotless.SerializedFunction
import java.io.File
import java.io.Serializable
import java.nio.file.Path
import java.util.concurrent.ConcurrentHashMap

/**
 * A Spotless step that reports markdown-lint errors as lints, using the built-in rules with their default settings:
 *
 * ```
 * spotless {
 *     format("markdown") {
 *         target("**&#47;*.md", "**&#47;*.markdown")
 *         addStep(MarkdownLintStep.create())
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

    @JvmStatic
    fun create(): FormatterStep = FormatterStep.create(NAME, State(markdownLintVersion()), CreateFormatterFunc())

    /**
     * Spotless only re-checks files when a step's serialized state changes. The markdown-lint version is the state, so
     * upgrading markdown-lint re-checks files against its rules. It holds no paths, so it's the same on every machine.
     */
    private data class State(val version: String) : Serializable {
        private companion object {
            private const val serialVersionUID = 1L
        }
    }

    private class CreateFormatterFunc : SerializedFunction<State, FormatterFunc> {
        override fun apply(state: State): FormatterFunc = MarkdownLintFormatterFunc()

        private companion object {
            private const val serialVersionUID = 1L
        }
    }

    private fun markdownLintVersion() = MarkdownLinter::class.java.`package`?.implementationVersion ?: "unknown"
}

internal class MarkdownLintFormatterFunc : FormatterFunc {

    // Every default rule includes all files and excludes none, so any root gives the same result; one per file system
    private val linters = ConcurrentHashMap<Path, MarkdownLinter>()

    override fun apply(input: String): String = input

    override fun lint(content: String, file: File): List<Lint> {
        val path = file.toPath().toAbsolutePath()
        val root = path.root ?: path
        val linter = linters.getOrPut(root) { MarkdownLinter(root) }

        return linter.lint(path, content).map { it.toLint(content) }
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

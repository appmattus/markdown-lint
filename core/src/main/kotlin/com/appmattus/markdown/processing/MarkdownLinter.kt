package com.appmattus.markdown.processing

import com.appmattus.markdown.dsl.Config
import com.appmattus.markdown.errors.Error
import com.appmattus.markdown.filter.MultiPathFilter
import com.appmattus.markdown.rules.AllRules
import com.appmattus.markdown.rules.Rule
import java.nio.file.Path

/**
 * Lints the content of a single markdown file with [rules].
 *
 * Unlike [RuleProcessor], it doesn't find or read files, write reports or apply a failure threshold, so it suits tools
 * that already have a file's content, such as formatter integrations.
 *
 * [rootDir] is the root that each rule's `includes` and `excludes` patterns are matched against, and is usually the
 * project directory.
 */
class MarkdownLinter @JvmOverloads constructor(
    private val rootDir: Path,
    rules: List<Rule> = AllRules(Config()).rules
) {

    /**
     * Lints with the built-in rules, overridden by the rules in [config] in the same way as the Gradle plugin. Only
     * the rules of [config] are used; its reports, threshold and file filters apply to [RuleProcessor].
     */
    constructor(rootDir: Path, config: Config) : this(rootDir, AllRules(config).rules)

    private class FilteredRule(val rule: Rule, val includes: MultiPathFilter, val excludes: MultiPathFilter) {
        fun appliesTo(file: Path) = includes.matches(file) && !excludes.matches(file)
    }

    private val filteredRules = rules.filter { it.configuration.active }.map { rule ->
        FilteredRule(
            rule,
            MultiPathFilter(rule.configuration.includes, rootDir),
            MultiPathFilter(rule.configuration.excludes, rootDir)
        )
    }

    /**
     * Returns the errors found in [content]. [file] is the path of the file the content belongs to, which filename
     * rules check and rule filters are matched against; it is not read, so the content can differ from what is on disk.
     */
    fun lint(file: Path, content: String): List<Error> {
        val document = MarkdownDocument(file, ParserFactory.parser.parse(content))

        return filteredRules.filter { it.appliesTo(file) }.flatMap { it.rule.processDocument(document) }
    }
}

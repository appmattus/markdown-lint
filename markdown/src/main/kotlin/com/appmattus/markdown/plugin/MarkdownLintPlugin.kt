package com.appmattus.markdown.plugin

import com.appmattus.markdown.dsl.Config
import com.appmattus.markdown.processing.RuleProcessor
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

@Suppress("unused")
class MarkdownLintPlugin : Plugin<Project> {

    override fun apply(project: Project): Unit = project.run {
        val markdownlint = extensions.create("markdownlint", MarkdownLint::class.java)

        tasks.register("markdownlint") {
            it.group = "verification"
            it.description = "Runs lint checks on your markdown files."

            it.outputs.upToDateWhen { false }

            it.doLast {
                val reportsDir = File(buildDir, "reports/markdownlint").apply { mkdirs() }

                val config = Config(
                    markdownlint.rules.toList(), markdownlint.reports.toSet(),
                    markdownlint.threshold, markdownlint.includes.toList(), markdownlint.excludes.toList()
                )

                RuleProcessor(projectDir.toPath(), reportsDir.toPath()).process(config, System.out)
            }
        }
    }
}

package com.appmattus.markdown.plugin

import com.appmattus.markdown.processing.RuleProcessor
import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("unused")
class MarkdownLintPlugin : Plugin<Project> {

    override fun apply(project: Project): Unit = project.run {
        val markdownlint = extensions.create("markdownlint", MarkdownLint::class.java)

        tasks.register("markdownlint") {
            it.group = "verification"
            it.description = "Runs lint checks on your markdown files."

            it.outputs.upToDateWhen { false }

            // Capture only what the action needs, as the configuration cache cannot serialize the Project
            val projectDir = projectDir
            val reportsDirectory = layout.buildDirectory.dir("reports/markdownlint")

            // Evaluated when the configuration cache is stored, so each rule's setup lambda is resolved beforehand
            val config = providers.provider {
                markdownlint.build().also { config ->
                    config.rules.forEach { rule -> rule.configuration }
                }
            }

            it.doLast {
                val reportsDir = reportsDirectory.get().asFile.apply { mkdirs() }

                RuleProcessor(projectDir.toPath(), reportsDir.toPath()).process(config.get(), System.out)
            }
        }
    }
}

package com.appmattus.markdown.plugin

import com.appmattus.markdown.processing.RuleProcessor
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

@Suppress("unused")
class MarkdownLintPlugin : Plugin<Project> {

    override fun apply(project: Project): Unit = project.run {
        val markdownlint = extensions.create("markdownlint", MarkdownLint::class.java)

        tasks.register("markdownlint") {
            it.outputs.upToDateWhen { false }

            it.doLast {
                val reportsDir = File(buildDir, "reports/markdownlint").apply { mkdirs() }

                RuleProcessor(projectDir, reportsDir)
                    .process(markdownlint.configFile, System.out)
            }
        }
    }
}

package com.appmattus.markdown.plugin

import com.appmattus.markdown.RuleProcessor
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.invoke
import java.io.File

@Suppress("unused")
class MarkdownLintPlugin : Plugin<Project> {

    override fun apply(project: Project): Unit = project.run {
        val markdownlint = extensions.create("markdownlint", MarkdownLint::class.java)

        tasks {
            register("markdownlint") {
                outputs.upToDateWhen { false }

                doLast {
                    val reportsDir = File(buildDir, "reports/markdownlint").apply { mkdirs() }

                    RuleProcessor().process(markdownlint.configFile, projectDir, reportsDir, System.out)
                }
            }
        }
    }
}

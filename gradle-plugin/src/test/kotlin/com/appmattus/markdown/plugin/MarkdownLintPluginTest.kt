package com.appmattus.markdown.plugin

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.rules.TemporaryFolder
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.io.File

object MarkdownLintPluginTest : Spek({
    val temporaryFolder = TemporaryFolder()

    beforeGroup {
        temporaryFolder.create()
    }

    afterGroup {
        temporaryFolder.delete()
    }

    Feature("MarkdownLintPlugin") {

        Scenario("") {
            lateinit var output: String

            Given("build script with default configuration") {
                temporaryFolder.createBuildScriptWithDefaultConfig()
            }

            And("a kts configuration script") {
                temporaryFolder.createFile("markdownlint.kts") {
                    """
                    import com.appmattus.markdown.markdownlint
                    import com.appmattus.markdown.rules.ConsistentHeaderStyleRule
                    import com.appmattus.markdown.rules.FirstHeaderH1Rule

                    markdownlint {
                        rules {
                            +FirstHeaderH1Rule(level = 2) {
                                active = true
                            }
                            +ConsistentHeaderStyleRule {
                                active = true
                            }
                        }

                        reports {
                            html()
                            checkstyle()
                        }
                    }
                    """.trimIndent()
                }
            }

            And("a markdown file") {
                temporaryFolder.createFile("README.md") {
                    """
                        ## Welcome to my project

                        This is the introduction

                           ## Section 2

                        This is the next section
                    """.trimIndent()
                }
            }

            And("a markdown file") {
                //temporaryFolder.newFolder("build")
                temporaryFolder.createFile("in-a-directory.md") {
                    """
                        # Welcome to my project

                        This is the introduction

                        ## Section 2

                        This is the next section
                    """.trimIndent()
                }
            }

            When("we execute the markdownlint task") {
                output = build(temporaryFolder, "markdownlint", "-q", "--stacktrace").output.trimEnd()
            }

            Then("the output matches") {
                //println(output)
                println(File(temporaryFolder.root, "build/reports/markdownlint.xml").readText().trim())
                //assertThat(output).isEqualTo(message)
            }
        }
    }
})

private fun TemporaryFolder.createBuildScriptWithDefaultConfig() {
    createFile("build.gradle.kts") {
        """
        plugins {
            id("markdownlint")
        }
        """.trimIndent()
    }
}

private fun TemporaryFolder.createBuildScriptWithConfigFile() {
    createFile("build.gradle.kts") {
        """
        plugins {
            id("markdownlint")
        }
        markdownlint {
            configFile = File(projectDir, "markdownlint.kts")
        }
        """.trimIndent()
    }
}


private fun build(temporaryFolder: TemporaryFolder, vararg arguments: String): BuildResult =
    GradleRunner
        .create()
        .withProjectDir(temporaryFolder.root)
        .withPluginClasspath()
        .withArguments(*arguments)
//            .withDebug(true)
        .build()

fun TemporaryFolder.createFile(filename: String, content: () -> String): File {
    return newFile(filename).apply {
        writeText(content())
    }
}

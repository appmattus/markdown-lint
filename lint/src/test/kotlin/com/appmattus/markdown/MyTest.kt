package com.appmattus.markdown

import com.appmattus.Kts
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object MyTest : Spek({
    Feature("MyTest") {
        Scenario("1") {
            Given("hey") {

                // read in markdown files with .md and .markdown file extensions


                val kts = MarkdownDocument::class.java.classLoader.getResource("markdown-lint.kts")
                    .readText(Charsets.UTF_8)

                println(Kts.eval(kts))
            }
        }
    }
})

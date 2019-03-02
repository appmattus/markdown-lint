package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.flextrade.jfixture.JFixture
import com.vladsch.flexmark.util.ast.Document
import org.assertj.core.api.Assertions
import org.mockito.Mockito
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object NoSurroundingHyphensFilenameRuleTest : Spek({
    Feature("NoSurroundingHyphensFilenameRule") {

        val rule by memoized { NoSurroundingHyphensFilenameRule() }
        val mockDocument = Mockito.mock(Document::class.java)

        Scenario("no hyphens in filename") {
            lateinit var document: MarkdownDocument

            Given("a document with no hyphens in filename") {
                document = MarkdownDocument("abcdefgh.md", mockDocument)
            }

            When("we visit the document") {
                rule.visitDocument(document)
            }

            Then("we have no errors") {
                Assertions.assertThat(rule.errors).isEmpty()
            }
        }

        Scenario("hyphen at start of filename") {
            lateinit var document: MarkdownDocument

            Given("a document with hyphen at start of filename") {
                val whitespace = JFixture().create().fromList("_", ".", "?")
                document = MarkdownDocument("-hello world.md", mockDocument)
            }

            When("we visit the document") {
                rule.visitDocument(document)
            }

            Then("we have 1 error") {
                Assertions.assertThat(rule.errors).size().isOne
            }
        }

        Scenario("hyphen at end of filename with extension") {
            lateinit var document: MarkdownDocument

            Given("a document with hyphen at end of filename") {
                val whitespace = JFixture().create().fromList("_", ".", "?")
                document = MarkdownDocument("hello world-.md", mockDocument)
            }

            When("we visit the document") {
                rule.visitDocument(document)
            }

            Then("we have 1 error") {
                Assertions.assertThat(rule.errors).size().isOne
            }
        }

        Scenario("hyphen at end of filename without extension") {
            lateinit var document: MarkdownDocument

            Given("a document with hyphen at end of filename") {
                val whitespace = JFixture().create().fromList("_", ".", "?")
                document = MarkdownDocument("hello world-", mockDocument)
            }

            When("we visit the document") {
                rule.visitDocument(document)
            }

            Then("we have 1 error") {
                Assertions.assertThat(rule.errors).size().isOne
            }
        }

        Scenario("hyphen at start and end of filename with extension") {
            lateinit var document: MarkdownDocument

            Given("a document with hyphen at start and end of filename with extension") {
                val whitespace = JFixture().create().fromList("_", ".", "?")
                document = MarkdownDocument("-hello world-.md", mockDocument)
            }

            When("we visit the document") {
                rule.visitDocument(document)
            }

            Then("we have 1 error") {
                Assertions.assertThat(rule.errors).size().isOne
            }
        }

        Scenario("hyphen at start and end of filename without extension") {
            lateinit var document: MarkdownDocument

            Given("a document with hyphen at start and end of filename with extension") {
                val whitespace = JFixture().create().fromList("_", ".", "?")
                document = MarkdownDocument("-hello world-", mockDocument)
            }

            When("we visit the document") {
                rule.visitDocument(document)
            }

            Then("we have 1 error") {
                Assertions.assertThat(rule.errors).size().isOne
            }
        }

        Scenario("empty filename without extension") {
            lateinit var document: MarkdownDocument

            Given("a document with empty filename without extension") {
                document = MarkdownDocument("", mockDocument)
            }

            When("we visit the document") {
                rule.visitDocument(document)
            }

            Then("we have no errors") {
                Assertions.assertThat(rule.errors).isEmpty()
            }
        }

        Scenario("empty filename with markdown extension") {
            lateinit var document: MarkdownDocument

            Given("a document with empty filename with markdown extension") {
                document = MarkdownDocument(".md", mockDocument)
            }

            When("we visit the document") {
                rule.visitDocument(document)
            }

            Then("we have no errors") {
                Assertions.assertThat(rule.errors).isEmpty()
            }
        }
    }
})

package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.flextrade.jfixture.JFixture
import com.vladsch.flexmark.util.ast.Document
import org.assertj.core.api.Assertions.assertThat
import org.mockito.Mockito
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.util.UUID

object NoWhitespaceFilenameRuleTest : Spek({

    Feature("NoWhitespaceFilenameRule") {
        val rule by memoized { NoWhitespaceFilenameRule() }
        val mockDocument = Mockito.mock(Document::class.java)

        Scenario("no whitespace filename") {
            lateinit var document: MarkdownDocument

            Given("a document with no whitespace filename") {
                val filename = UUID.randomUUID().toString().toLowerCase()
                document = MarkdownDocument(filename, mockDocument)
            }

            When("we visit the document") {
                rule.visitDocument(document)
            }

            Then("we have no errors") {
                assertThat(rule.errors).isEmpty()
            }
        }

        Scenario("whitespace in filename") {
            lateinit var document: MarkdownDocument

            Given("a document with whitespace in filename") {
                val whitespace = JFixture().create().fromList(" ", "\t", "\n", "\r")
                document = MarkdownDocument("hello${whitespace}world.md", mockDocument)
            }

            When("we visit the document") {
                rule.visitDocument(document)
            }

            Then("we have 1 error") {
                assertThat(rule.errors).size().isOne
            }
        }

        Scenario("empty filename") {
            lateinit var document: MarkdownDocument

            Given("a document with empty filename") {
                document = MarkdownDocument("", mockDocument)
            }

            When("we visit the document") {
                rule.visitDocument(document)
            }

            Then("we have no errors") {
                assertThat(rule.errors).isEmpty()
            }
        }
    }
})

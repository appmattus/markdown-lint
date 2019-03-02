package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.vladsch.flexmark.util.ast.Document
import org.assertj.core.api.Assertions.assertThat
import org.mockito.Mockito
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.util.UUID

object LowerCaseFilenameRuleTest : Spek({

    Feature("LowerCaseFilenameRule") {
        val rule by memoized { LowerCaseFilenameRule() }
        val mockDocument = Mockito.mock(Document::class.java)

        Scenario("all lowercase filename") {
            lateinit var document: MarkdownDocument

            Given("a document with lowercase filename") {
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

        Scenario("uppercase in filename") {
            lateinit var document: MarkdownDocument

            Given("a document with uppercase filename") {
                val filename = UUID.randomUUID().toString().toUpperCase()
                document = MarkdownDocument(filename, mockDocument)
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

package com.appmattus.markdown.rules

import com.appmattus.markdown.Error
import com.appmattus.markdown.MarkdownDocument
import mockDocument
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.util.UUID

object LowerCaseFilenameRuleTest : Spek({

    Feature("LowerCaseFilenameRule") {
        val rule by memoized { LowerCaseFilenameRule() }
        val mockDocument = mockDocument()

        Scenario("all lowercase filename") {
            lateinit var document: MarkdownDocument
            lateinit var ruleErrors: List<Error>

            Given("a document with lowercase filename") {
                val filename = UUID.randomUUID().toString().toLowerCase()
                document = MarkdownDocument(filename, mockDocument)
            }

            When("we visit the document") {
                ruleErrors = rule.processDocument(document)
            }

            Then("we have no errors") {
                assertThat(ruleErrors).isEmpty()
            }
        }

        Scenario("uppercase in filename") {
            lateinit var document: MarkdownDocument
            lateinit var ruleErrors: List<Error>

            Given("a document with uppercase filename") {
                val filename = UUID.randomUUID().toString().toUpperCase()
                document = MarkdownDocument(filename, mockDocument)
            }

            When("we visit the document") {
                ruleErrors = rule.processDocument(document)
            }

            Then("we have 1 error") {
                assertThat(ruleErrors).size().isOne
            }
        }

        Scenario("empty filename") {
            lateinit var document: MarkdownDocument
            lateinit var ruleErrors: List<Error>

            Given("a document with empty filename") {
                document = MarkdownDocument("", mockDocument)
            }

            When("we visit the document") {
                ruleErrors = rule.processDocument(document)
            }

            Then("we have no errors") {
                assertThat(ruleErrors).isEmpty()
            }
        }
    }
})

package com.appmattus.markdown.rules

import com.appmattus.markdown.MarkdownDocument
import com.flextrade.jfixture.JFixture
import com.vladsch.flexmark.util.ast.Document
import org.assertj.core.api.Assertions
import org.mockito.Mockito
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.util.UUID

object NoPunctuationFilenameRuleTest : Spek({
    Feature("NoPunctuationFilenameRule") {

        val rule by memoized { NoPunctuationFilenameRule() }
        val mockDocument = Mockito.mock(Document::class.java)

        Scenario("no punctuation in filename") {
            lateinit var document: MarkdownDocument

            Given("a document with no punctuation in filename") {
                val filename = UUID.randomUUID().toString().toLowerCase()
                document = MarkdownDocument(filename, mockDocument)
            }

            When("we visit the document") {
                rule.visitDocument(document)
            }

            Then("we have no errors") {
                Assertions.assertThat(rule.errors).isEmpty()
            }
        }

        Scenario("punctuation in filename") {
            lateinit var document: MarkdownDocument

            Given("a document with punctuation in filename") {
                val whitespace = JFixture().create().fromList("_", ".", "?")
                document = MarkdownDocument("hello${whitespace}world.md", mockDocument)
            }

            When("we visit the document") {
                rule.visitDocument(document)
            }

            Then("we have 1 error") {
                Assertions.assertThat(rule.errors).size().isOne
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
                Assertions.assertThat(rule.errors).isEmpty()
            }
        }
    }
})

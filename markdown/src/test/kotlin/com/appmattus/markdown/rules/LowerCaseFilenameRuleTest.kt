package com.appmattus.markdown.rules

import com.appmattus.gherkin.dsl.Given
import com.appmattus.gherkin.dsl.Then
import com.appmattus.gherkin.dsl.When
import com.appmattus.gherkin.jupiter.gherkin
import com.appmattus.markdown.errors.Error
import com.appmattus.markdown.processing.MarkdownDocument
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.TestFactory
import java.io.File
import java.util.UUID

class LowerCaseFilenameRuleTest {

    private val rule = LowerCaseFilenameRule()
    private val mockDocument = mockDocument()

    @TestFactory
    fun `all lowercase filename`() = gherkin {
        val document: MarkdownDocument
        val ruleErrors: List<Error>

        Given("a document with lowercase filename") {
            val filename = UUID.randomUUID().toString().toLowerCase()
            document = MarkdownDocument(File(filename), mockDocument)
        }

        When("we visit the document") {
            ruleErrors = rule.processDocument(document)
        }

        Then("we have no errors") {
            assertThat(ruleErrors).isEmpty()
        }
    }

    @TestFactory
    fun `uppercase in filename`() = gherkin {
        val document: MarkdownDocument
        val ruleErrors: List<Error>

        Given("a document with uppercase filename") {
            val filename = UUID.randomUUID().toString().toUpperCase()
            document = MarkdownDocument(File(filename), mockDocument)
        }

        When("we visit the document") {
            ruleErrors = rule.processDocument(document)
        }

        Then("we have 1 error") {
            assertThat(ruleErrors).size().isOne
        }
    }

    @TestFactory
    fun `empty filename`() = gherkin {
        val document: MarkdownDocument
        val ruleErrors: List<Error>

        Given("a document with empty filename") {
            document = MarkdownDocument(File(""), mockDocument)
        }

        When("we visit the document") {
            ruleErrors = rule.processDocument(document)
        }

        Then("we have no errors") {
            assertThat(ruleErrors).isEmpty()
        }
    }
}

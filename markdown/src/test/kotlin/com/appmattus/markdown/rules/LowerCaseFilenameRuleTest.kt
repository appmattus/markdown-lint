package com.appmattus.markdown.rules

import com.appmattus.markdown.errors.Error
import com.appmattus.markdown.processing.MarkdownDocument
import mockDocument
import org.assertj.core.api.Assertions.assertThat
import java.nio.file.Paths
import java.util.UUID
import kotlin.test.Test

class LowerCaseFilenameRuleTest {
    private val rule by lazy { LowerCaseFilenameRule() }
    private val mockDocument = mockDocument()

    @Test
    fun `all lowercase filename`() {
        lateinit var document: MarkdownDocument
        lateinit var ruleErrors: List<Error>

        // Given a document with lowercase filename
        val filename = UUID.randomUUID().toString().lowercase()
        document = MarkdownDocument(Paths.get(filename), mockDocument)

        // When we visit the document
        ruleErrors = rule.processDocument(document)

        // Then we have no errors
        assertThat(ruleErrors).isEmpty()
    }

    @Test
    fun `uppercase in filename`() {
        lateinit var document: MarkdownDocument
        lateinit var ruleErrors: List<Error>

        // Given a document with uppercase filename
        val filename = UUID.randomUUID().toString().uppercase()
        document = MarkdownDocument(Paths.get(filename), mockDocument)

        // When we visit the document
        ruleErrors = rule.processDocument(document)

        // Then we have 1 error
        assertThat(ruleErrors).size().isOne
    }

    @Test
    fun `empty filename`() {
        lateinit var document: MarkdownDocument
        lateinit var ruleErrors: List<Error>

        // Given a document with empty filename
        document = MarkdownDocument(Paths.get(""), mockDocument)

        // When we visit the document
        ruleErrors = rule.processDocument(document)

        // Then we have no errors
        assertThat(ruleErrors).isEmpty()
    }
}

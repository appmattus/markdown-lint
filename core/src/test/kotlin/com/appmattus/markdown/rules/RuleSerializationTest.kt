package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.Config
import com.appmattus.markdown.loadDocumentUnixEol
import com.appmattus.markdown.rules.config.CodeBlockStyle
import com.appmattus.markdown.rules.config.EmphasisStyle
import com.appmattus.markdown.rules.config.HeaderStyle
import com.appmattus.markdown.rules.config.HorizontalRuleStyle
import com.appmattus.markdown.rules.config.OrderedListStyle
import com.appmattus.markdown.rules.config.TaskListItemMarkerStyle
import com.appmattus.markdown.rules.config.UnorderedListStyle
import org.assertj.core.api.Assertions.assertThat
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import kotlin.test.Test

class RuleSerializationTest {

    private val documents = allFiles.map { loadDocumentUnixEol(it) }

    private fun <T : Rule> T.roundTrip(): Rule {
        // Rules are stored after their setup is resolved, as the Spotless step does
        configuration
        val bytes = ByteArrayOutputStream().also { ObjectOutputStream(it).use { out -> out.writeObject(this) } }
        return ObjectInputStream(ByteArrayInputStream(bytes.toByteArray())).use { it.readObject() } as Rule
    }

    private fun assertSameErrorsAfterRoundTrip(rule: Rule) {
        val restored = rule.roundTrip()

        documents.forEach { document ->
            assertThat(restored.processDocument(document))
                .describedAs("${rule::class.simpleName} on ${document.filename}")
                .isEqualTo(rule.processDocument(document))
        }
    }

    @Test
    fun `every default rule reports the same errors after serialization`() {
        // Given every built-in rule with its default settings
        val rules = AllRules(Config()).rules

        // Then each reports the same errors on every test file once serialized and restored
        rules.forEach { assertSameErrorsAfterRoundTrip(it) }
    }

    @Test
    fun `rules with each style setting report the same errors after serialization`() {
        // Given the rules that take a style, with every style
        val rules = CodeBlockStyle.values().map { CodeBlockStyleRule(it) } +
            EmphasisStyle.values().map { ConsistentEmphasisStyleRule(it) } +
            HeaderStyle.values().map { ConsistentHeaderStyleRule(it) } +
            TaskListItemMarkerStyle.values().map { ConsistentTaskListMarkerStyleRule(it) } +
            UnorderedListStyle.values().map { ConsistentUlStyleRule(it) } +
            OrderedListStyle.values().map { OlPrefixRule(it) } +
            listOf(
                HorizontalRuleStyle.Consistent,
                HorizontalRuleStyle.Asterisk,
                HorizontalRuleStyle.Dash,
                HorizontalRuleStyle.Underscore,
                HorizontalRuleStyle.Exact("_____")
            ).map { HrStyleRule(it) }

        // Then each reports the same errors, including messages that name the style, once restored
        rules.forEach { assertSameErrorsAfterRoundTrip(it) }
    }

    @Test
    fun `rule setup is kept after serialization`() {
        // Given a rule with setup from its constructor lambda
        val rule = SingleH1Rule {
            active = false
            excludes = listOf(".*/docs/.*")
        }

        // When it is serialized and restored
        val restored = rule.roundTrip()

        // Then its resolved setup is kept, although the lambda itself isn't stored
        assertThat(restored.configuration).isEqualTo(rule.configuration)
    }
}

// Outside the library package, as custom rules are, so only public API is used
package com.example.customrule

import com.appmattus.markdown.dsl.Config
import com.appmattus.markdown.dsl.RuleSetup
import com.appmattus.markdown.dsl.RulesBuilder
import com.appmattus.markdown.errors.ErrorReporter
import com.appmattus.markdown.processing.MarkdownDocument
import com.appmattus.markdown.rules.AllRules
import com.appmattus.markdown.rules.Rule
import com.appmattus.markdown.rules.SingleH1Rule
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class CustomRuleTest {

    private class NoSetupRule : Rule() {
        override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) = Unit
    }

    private class ConstructorSetupRule(config: RuleSetup.Builder.() -> Unit = {}) : Rule(config) {
        override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) = Unit
    }

    private class OverriddenSetupRule(override val config: RuleSetup.Builder.() -> Unit = {}) : Rule() {
        override fun visitDocument(document: MarkdownDocument, errorReporter: ErrorReporter) = Unit
    }

    @Test
    fun `custom rule without setup uses the default configuration`() {
        lateinit var rule: Rule

        // Given a custom rule that doesn't take any setup
        rule = NoSetupRule()

        // Then it uses the default configuration
        assertThat(rule.configuration).isEqualTo(RuleSetup.Builder().build())
    }

    @Test
    fun `custom rule passes its setup to the Rule constructor`() {
        lateinit var rule: Rule

        // Given a custom rule configured through its constructor
        rule = ConstructorSetupRule {
            active = false
            includes = listOf(".*/docs/.*")
        }

        // Then the configuration comes from that setup
        assertThat(rule.configuration.active).isFalse()
        assertThat(rule.configuration.includes).containsExactly(".*/docs/.*")
    }

    @Test
    fun `custom rule overriding config still works`() {
        lateinit var rule: Rule

        // Given a custom rule written before setup moved to the Rule constructor
        rule = OverriddenSetupRule { excludes = listOf(".*/ignored.md") }

        // Then its overridden setup is still applied
        assertThat(rule.configuration.excludes).containsExactly(".*/ignored.md")
    }

    @Test
    fun `custom rule is added to the default rules`() {
        lateinit var rules: List<Rule>

        // Given a config with a custom rule and setup from the rules builder
        val config = Config(
            rules = RulesBuilder().apply {
                rule(NoSetupRule()) { excludes = listOf(".*/ignored.md") }
            }.build()
        )

        // When we ask for the rules
        rules = AllRules(config).rules

        // Then the custom rule runs alongside the built-in rules
        val custom = rules.filterIsInstance<NoSetupRule>().single()
        assertThat(rules).anyMatch { it is SingleH1Rule }

        // And the rules builder setup is applied to it
        assertThat(custom.configuration.excludes).containsExactly(".*/ignored.md")
    }
}

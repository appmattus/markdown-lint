package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.Config
import com.appmattus.markdown.dsl.RulesBuilder
import com.appmattus.markdown.processing.MarkdownDocument
import emptyDocument
import io.github.classgraph.ClassGraph
import org.assertj.core.api.Assertions.assertThat
import java.nio.file.Paths
import kotlin.test.Test

class AllRulesTest {
    private val scanResult = ClassGraph().enableAllInfo().acceptPackages("com.appmattus.markdown").scan()
    private val allExpectedRules = scanResult.getSubclasses(Rule::class.java.name).loadClasses()

    @Test
    fun `empty configuration returns all rules`() {
        lateinit var config: Config
        lateinit var rules: List<Rule>

        // Given an empty rule config
        config = Config(rules = RulesBuilder().build())

        // When we ask for the rules
        rules = AllRules(config).rules

        // Then all rules are returned
        val missing = allExpectedRules.filter { clazz ->
            rules.find { rule -> rule::class.java == clazz } == null
        }.map {
            it.simpleName
        }.toString()

        assertThat(rules.size).`as`("Missing $missing")
            .isEqualTo(allExpectedRules.size)
    }

    @Test
    fun `config disables rules`() {
        lateinit var config: Config
        lateinit var rules: List<Rule>

        // Given config disables one rule
        config = Config(
            rules = RulesBuilder().apply {
                +BlanksAroundFencesRule {
                    active = false
                }
            }.build()
        )

        // When we ask for the rules
        rules = AllRules(config).rules

        // Then one less rule is returned
        assertThat(rules.size).isEqualTo(allExpectedRules.size - 1)

        // And all rules are active
        assertThat(rules).allMatch { it.configuration.active }
    }

    @Test
    fun `replacement rule configuration`() {
        lateinit var config: Config
        lateinit var rules: List<Rule>

        // Given we configure one rule
        config = Config(
            rules = RulesBuilder().apply {
                +NoPunctuationFilenameRule(punctuation = "ABC")
            }.build()
        )

        // When we ask for the rules
        rules = AllRules(config).rules

        // Then all rules are returned
        assertThat(rules.size).isEqualTo(allExpectedRules.size)

        // And the specified rule does not trigger as expected
        val rule = rules.filterIsInstance(NoPunctuationFilenameRule::class.java).first()
        val errorsForZ = rule.processDocument(MarkdownDocument(Paths.get("Z"), emptyDocument()))
        assertThat(errorsForZ).size().isZero

        // And the specified rule triggers as expected
        val errorsForA = rule.processDocument(MarkdownDocument(Paths.get("A"), emptyDocument()))
        assertThat(errorsForA).size().isOne

        // And the specified rule result differs to default config
        val defaultRule = NoPunctuationFilenameRule()
        val defaultErrors = defaultRule.processDocument(MarkdownDocument(Paths.get("A"), emptyDocument()))
        assertThat(defaultErrors).size().isZero
    }
}

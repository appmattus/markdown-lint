package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.Config
import com.appmattus.markdown.plugin.MarkdownLint
import com.appmattus.markdown.processing.MarkdownDocument
import io.github.classgraph.ClassGraph
import mockDocument
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.io.File

object AllRulesTest : Spek({
    Feature("AllRules") {
        val mockDocument = mockDocument()
        val scanResult = ClassGraph().enableAllInfo().whitelistPackages("com.appmattus.markdown").scan()
        val allExpectedRules = scanResult.getSubclasses(Rule::class.java.name).loadClasses()

        Scenario("empty configuration returns all rules") {
            lateinit var config: Config
            lateinit var rules: List<Rule>

            Given("an empty rule config") {
                config = MarkdownLint().apply {
                    rules { }
                }.build()
            }

            When("we ask for the rules") {
                rules = AllRules(config).rules
            }

            Then("all rules are returned") {
                val missing = allExpectedRules.filter { clazz ->
                    rules.find { rule -> rule::class.java == clazz } == null
                }.map {
                    it.simpleName
                }.toString()

                assertThat(rules.size).`as`("Missing $missing")
                    .isEqualTo(allExpectedRules.size)
            }
        }

        Scenario("config disables rules") {
            lateinit var config: Config
            lateinit var rules: List<Rule>

            Given("config disables one rule") {
                config = MarkdownLint().apply {
                    rules {
                        +BlanksAroundFencesRule {
                            active = false
                        }
                    }
                }.build()
            }

            When("we ask for the rules") {
                rules = AllRules(config).rules
            }

            Then("one less rule is returned") {
                assertThat(rules.size).isEqualTo(allExpectedRules.size - 1)
            }

            And("all rules are active") {
                assertThat(rules).allMatch { it.configuration.active }
            }
        }

        Scenario("replacement rule configuration") {
            lateinit var config: Config
            lateinit var rules: List<Rule>

            Given("we configure one rule") {
                config = MarkdownLint().apply {
                    rules {
                        +NoPunctuationFilenameRule(punctuation = "ABC")
                    }
                }.build()
            }

            When("we ask for the rules") {
                rules = AllRules(config).rules
            }

            Then("all rules are returned") {
                assertThat(rules.size).isEqualTo(allExpectedRules.size)
            }

            And("the specified rule does not trigger as expected") {
                val rule = rules.filterIsInstance(NoPunctuationFilenameRule::class.java).first()
                val errors = rule.processDocument(MarkdownDocument(File("Z"), mockDocument))
                assertThat(errors).size().isZero
            }

            And("the specified rule triggers as expected") {
                val rule = rules.filterIsInstance(NoPunctuationFilenameRule::class.java).first()
                val errors = rule.processDocument(MarkdownDocument(File("A"), mockDocument))
                assertThat(errors).size().isOne
            }

            And("the specified rule result differs to default config") {
                val rule = NoPunctuationFilenameRule()
                val errors = rule.processDocument(MarkdownDocument(File("A"), mockDocument))
                assertThat(errors).size().isZero
            }
        }
    }
})

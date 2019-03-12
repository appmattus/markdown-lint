package com.appmattus.markdown.rules

import com.appmattus.markdown.dsl.MarkdownLintConfig
import com.appmattus.markdown.dsl.markdownLintConfig
import com.appmattus.markdown.processing.MarkdownDocument
import mockDocument
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object AllRulesTest : Spek({
    Feature("AllRules") {
        val mockDocument = mockDocument()

        Scenario("empty configuration returns all rules") {
            lateinit var config: MarkdownLintConfig
            lateinit var rules: List<Rule>

            Given("an empty rule config") {
                config = markdownLintConfig {
                    rules { }
                }
            }

            When("we ask for the rules") {
                rules = AllRules(config).rules
            }

            Then("all rules are returned") {
                assertThat(rules).size().isEqualTo(45)
            }
        }

        Scenario("config disables rules") {
            lateinit var config: MarkdownLintConfig
            lateinit var rules: List<Rule>

            Given("config disables one rule") {
                config = markdownLintConfig {
                    rules {
                        +BlanksAroundFencesRule {
                            active = false
                        }
                    }
                }
            }

            When("we ask for the rules") {
                rules = AllRules(config).rules
            }

            Then("one less rule is returned") {
                assertThat(rules).size().isEqualTo(44)
            }

            And("all rules are active") {
                assertThat(rules).allMatch { it.configuration.active }
            }
        }

        Scenario("replacement rule configuration") {
            lateinit var config: MarkdownLintConfig
            lateinit var rules: List<Rule>

            Given("we configure one rule") {
                config = markdownLintConfig {
                    rules {
                        +NoPunctuationFilenameRule(punctuation = "ABC")
                    }
                }
            }

            When("we ask for the rules") {
                rules = AllRules(config).rules
            }

            Then("all rules are returned") {
                assertThat(rules).size().isEqualTo(45)
            }

            And("the specified rule does not trigger as expected") {
                val rule = rules.filterIsInstance(NoPunctuationFilenameRule::class.java).first()
                val errors = rule.processDocument(MarkdownDocument("Z", mockDocument))
                assertThat(errors).size().isZero
            }

            And("the specified rule triggers as expected") {
                val rule = rules.filterIsInstance(NoPunctuationFilenameRule::class.java).first()
                val errors = rule.processDocument(MarkdownDocument("A", mockDocument))
                assertThat(errors).size().isOne
            }

            And("the specified rule result differs to default config") {
                val rule = NoPunctuationFilenameRule()
                val errors = rule.processDocument(MarkdownDocument("A", mockDocument))
                assertThat(errors).size().isZero
            }
        }
    }
})

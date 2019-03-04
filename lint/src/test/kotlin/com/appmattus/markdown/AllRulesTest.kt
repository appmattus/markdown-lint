package com.appmattus.markdown

import com.appmattus.markdown.rules.BlanksAroundFencesRule
import com.appmattus.markdown.rules.NoPunctuationFilenameRule
import com.nhaarman.mockitokotlin2.mock
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object AllRulesTest : Spek({
    Feature("AllRules") {
        Scenario("empty configuration returns all rules") {
            lateinit var config: MarkdownLintConfig
            lateinit var rules: List<Rule>

            Given("an empty rule config") {
                config = markdownlint {
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
                config = markdownlint {
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
                config = markdownlint {
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
                rule.visitDocument(MarkdownDocument("Z", mock()))
                assertThat(rule.errors).size().isZero
            }

            And("the specified rule triggers as expected") {
                val rule = rules.filterIsInstance(NoPunctuationFilenameRule::class.java).first()
                rule.visitDocument(MarkdownDocument("A", mock()))
                assertThat(rule.errors).size().isOne
            }

            And("the specified rule result differs to default config") {
                val rule = NoPunctuationFilenameRule()
                rule.visitDocument(MarkdownDocument("A", mock()))
                assertThat(rule.errors).size().isZero
            }
        }
    }
})

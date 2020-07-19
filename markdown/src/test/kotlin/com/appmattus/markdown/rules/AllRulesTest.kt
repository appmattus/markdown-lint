/*
 * Copyright 2020 Appmattus Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.appmattus.markdown.rules

import com.appmattus.gherkin.dsl.And
import com.appmattus.gherkin.dsl.Given
import com.appmattus.gherkin.dsl.Then
import com.appmattus.gherkin.dsl.When
import com.appmattus.gherkin.jupiter.gherkin
import com.appmattus.markdown.dsl.Config
import com.appmattus.markdown.plugin.MarkdownLint
import com.appmattus.markdown.processing.MarkdownDocument
import io.github.classgraph.ClassGraph
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.TestFactory
import java.io.File

class AllRulesTest {

    companion object {
        private val mockDocument = mockDocument()
        private val scanResult = ClassGraph().enableAllInfo().acceptPackages("com.appmattus.markdown").scan()
        private val allExpectedRules = scanResult.getSubclasses(Rule::class.java.name).loadClasses()
    }

    @TestFactory
    fun `empty configuration returns all rules`() = gherkin {
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

    @TestFactory
    fun `config disables rules`() = gherkin {
        val config: Config
        val rules: List<Rule>

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

    @TestFactory
    fun `replacement rule configuration`() = gherkin {
        val config: Config
        val rules: List<Rule>

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

package com.appmattus.markdown.rules.extentions

import com.vladsch.flexmark.util.sequence.BasedSequence
import com.vladsch.flexmark.util.sequence.CharSubSequence
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object BasedSequenceExtTest : Spek({
    Feature("BasedSequenceExt") {
        Scenario("split a unix style string") {
            lateinit var string: BasedSequence
            lateinit var lines: Array<BasedSequence>

            Given("a string in unix style") {
                string = CharSubSequence.of("hello\nworld")
            }

            When("we split the string") {
                lines = string.splitIntoLines()
            }

            Then("the string is split into two") {
                assertThat(lines).hasSize(2)

                assertThat(lines[0].startOffset).isEqualTo(0)
                assertThat(lines[0].endOffset).isEqualTo(5)

                assertThat(lines[1].startOffset).isEqualTo(6)
                assertThat(lines[1].endOffset).isEqualTo(11)
            }
        }

        Scenario("split an old mac style string") {
            lateinit var string: BasedSequence
            lateinit var lines: Array<BasedSequence>

            Given("a string in old mac style") {
                string = CharSubSequence.of("hello\rworld")
            }

            When("we split the string") {
                lines = string.splitIntoLines()
            }

            Then("the string is split into two") {
                assertThat(lines).hasSize(2)

                assertThat(lines[0].startOffset).isEqualTo(0)
                assertThat(lines[0].endOffset).isEqualTo(5)

                assertThat(lines[1].startOffset).isEqualTo(6)
                assertThat(lines[1].endOffset).isEqualTo(11)
            }
        }

        Scenario("split a windows style string") {
            lateinit var string: BasedSequence
            lateinit var lines: Array<BasedSequence>

            Given("a string in windows style") {
                string = CharSubSequence.of("hello\r\nworld")
            }

            When("we split the string") {
                lines = string.splitIntoLines()
            }

            Then("the string is split into two") {
                assertThat(lines).hasSize(2)

                assertThat(lines[0].startOffset).isEqualTo(0)
                assertThat(lines[0].endOffset).isEqualTo(5)

                assertThat(lines[1].startOffset).isEqualTo(7)
                assertThat(lines[1].endOffset).isEqualTo(12)
            }
        }

        Scenario("split a unix style string with trailing eol") {
            lateinit var string: BasedSequence
            lateinit var lines: Array<BasedSequence>

            Given("a string in unix style") {
                string = CharSubSequence.of("hello\nworld\n")
            }

            When("we split the string") {
                lines = string.splitIntoLines()
            }

            Then("the string is split into two") {
                assertThat(lines).hasSize(2)

                assertThat(lines[0].startOffset).isEqualTo(0)
                assertThat(lines[0].endOffset).isEqualTo(5)

                assertThat(lines[1].startOffset).isEqualTo(6)
                assertThat(lines[1].endOffset).isEqualTo(11)
            }
        }

        Scenario("split an old mac style string with trailing eol") {
            lateinit var string: BasedSequence
            lateinit var lines: Array<BasedSequence>

            Given("a string in old mac style") {
                string = CharSubSequence.of("hello\rworld\r")
            }

            When("we split the string") {
                lines = string.splitIntoLines()
            }

            Then("the string is split into two") {
                assertThat(lines).hasSize(2)

                assertThat(lines[0].startOffset).isEqualTo(0)
                assertThat(lines[0].endOffset).isEqualTo(5)

                assertThat(lines[1].startOffset).isEqualTo(6)
                assertThat(lines[1].endOffset).isEqualTo(11)
            }
        }

        Scenario("split a windows style string with trailing eol") {
            lateinit var string: BasedSequence
            lateinit var lines: Array<BasedSequence>

            Given("a string in windows style") {
                string = CharSubSequence.of("hello\r\nworld\r\n")
            }

            When("we split the string") {
                lines = string.splitIntoLines()
            }

            Then("the string is split into two") {
                assertThat(lines).hasSize(2)

                assertThat(lines[0].startOffset).isEqualTo(0)
                assertThat(lines[0].endOffset).isEqualTo(5)

                assertThat(lines[1].startOffset).isEqualTo(7)
                assertThat(lines[1].endOffset).isEqualTo(12)
            }
        }
    }
})

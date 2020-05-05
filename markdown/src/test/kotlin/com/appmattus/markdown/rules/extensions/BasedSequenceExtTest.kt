package com.appmattus.markdown.rules.extensions

import com.appmattus.gherkin.dsl.Given
import com.appmattus.gherkin.dsl.Then
import com.appmattus.gherkin.dsl.When
import com.appmattus.gherkin.jupiter.gherkin
import com.vladsch.flexmark.util.sequence.BasedSequence
import com.vladsch.flexmark.util.sequence.CharSubSequence
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.TestFactory

class BasedSequenceExtTest {

    @TestFactory
    fun `split a unix style string`() = gherkin {
        val string: BasedSequence
        val lines: Array<BasedSequence>

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

    @TestFactory
    fun `split an old mac style string`() = gherkin {
        val string: BasedSequence
        val lines: Array<BasedSequence>

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

    @TestFactory
    fun `split a windows style string`() = gherkin {
        val string: BasedSequence
        val lines: Array<BasedSequence>

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

    @TestFactory
    fun `split a unix style string with trailing eol`() = gherkin {
        val string: BasedSequence
        val lines: Array<BasedSequence>

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

    @TestFactory
    fun `split an old mac style string with trailing eol`() = gherkin {
        val string: BasedSequence
        val lines: Array<BasedSequence>

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

    @TestFactory
    fun `split a windows style string with trailing eol`() = gherkin {
        val string: BasedSequence
        val lines: Array<BasedSequence>

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

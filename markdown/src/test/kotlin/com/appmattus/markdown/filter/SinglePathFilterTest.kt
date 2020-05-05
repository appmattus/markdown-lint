package com.appmattus.markdown.filter

import com.appmattus.gherkin.dsl.And
import com.appmattus.gherkin.dsl.Given
import com.appmattus.gherkin.dsl.Then
import com.appmattus.gherkin.dsl.When
import com.appmattus.gherkin.jupiter.gherkin
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.TestFactory
import java.nio.file.Path
import java.nio.file.Paths

class SinglePathFilterTest {

    @TestFactory
    fun `invalid regex pattern throws an IllegalArgumentException`() = gherkin {
        val pattern: String
        Given("an invalid regex pattern") {
            pattern = "*."
        }

        Then("SinglePathFilter throws an IllegalArgumentException") {
            assertThatIllegalArgumentException().isThrownBy { SinglePathFilter(pattern) }
        }
    }

    @TestFactory
    fun `empty regex pattern throws an IllegalArgumentException`() = gherkin {
        val pattern: String
        Given("an empty pattern") {
            pattern = ""
        }

        Then("SinglePathFilter throws an IllegalArgumentException") {
            assertThatIllegalArgumentException().isThrownBy { SinglePathFilter(pattern) }
        }
    }

    @TestFactory
    fun `blank regex pattern throws an IllegalArgumentException`() = gherkin {
        val pattern: String
        Given("a blank pattern") {
            pattern = "    "
        }

        Then("SinglePathFilter throws an IllegalArgumentException") {
            assertThatIllegalArgumentException().isThrownBy { SinglePathFilter(pattern) }
        }
    }

    @TestFactory
    fun `matches relative path`() = gherkin {
        val pattern: String
        val root: Path
        val path: Path

        Given("a single regex pattern on Unix systems") {
            pattern = ".*/build/.*"
        }

        And("a default root path") {
            root = Paths.get("").toAbsolutePath()
        }

        When("we have a relative path") {
            path = root.resolve("some/build/path/should/match")
        }

        Then("SinglePathFilter matches") {
            assertThat(SinglePathFilter(pattern).matches(path)).isTrue()
        }
    }

    @TestFactory
    fun `matches relative path with the filter in the beginning`() = gherkin {
        val pattern: String
        val root: Path
        val path: Path

        Given("a single regex pattern on Unix systems") {
            pattern = ".*/build/.*"
        }

        And("a default root path") {
            root = Paths.get("").toAbsolutePath()
        }

        When("we have a relative path with the filter in the beginning") {
            path = root.resolve("build/path/should/match")
        }

        Then("SinglePathFilter matches") {
            assertThat(SinglePathFilter(pattern).matches(path)).isTrue()
        }
    }

    @TestFactory
    fun `matches relative path with the filter in the beginning v2`() = gherkin {
        val pattern: String
        val root: Path
        val path: Path

        Given("a single regex pattern on Unix systems") {
            pattern = ".*/build/.*"
        }

        And("a default root path") {
            root = Paths.get("").toAbsolutePath()
        }

        When("we have a relative path with the filter in the beginning") {
            path = root.resolve("build/path/should/match")
        }

        Then("SinglePathFilter matches") {
            assertThat(SinglePathFilter(pattern).matches(path)).isTrue()
        }
    }

    @TestFactory
    fun `does not match an unrelated path`() = gherkin {
        val pattern: String
        val root: Path
        val path: Path

        Given("a single regex pattern on Unix systems") {
            pattern = ".*/build/.*"
        }

        And("a default root path") {
            root = Paths.get("").toAbsolutePath()
        }

        When("we have a path that should not match") {
            path = root.resolve("this/should/NOT/match")
        }

        Then("SinglePathFilter does not match") {
            assertThat(SinglePathFilter(pattern).matches(path)).isFalse()
        }
    }

    @TestFactory
    fun `does not match the pattern in the root path`() = gherkin {
        val pattern: String
        val root: Path
        val path: Path

        Given("a single regex pattern on Unix systems") {
            pattern = ".*/build/.*"
        }

        And("a root path with the pattern") {
            root = Paths.get("/tmp/build/detekt").toAbsolutePath()
        }

        When("we have a path that should not match") {
            path = root.resolve("should/not/match")
        }

        Then("SinglePathFilter does not match") {
            assertThat(SinglePathFilter(pattern, root).matches(path)).isFalse()
        }
    }

    @TestFactory
    fun `does not match the pattern in the absolute path with pattern but the relative path`() = gherkin {
        val pattern: String
        val root: Path
        val path: Path

        Given("a single regex pattern on Unix systems") {
            pattern = ".*/build/.*"
        }

        And("a root path with the pattern") {
            root = Paths.get("/tmp/build/detekt").toAbsolutePath()
        }

        When("we have a path that should match") {
            path = root.resolve("should/match/build/path")
        }

        Then("SinglePathFilter matches") {
            assertThat(SinglePathFilter(pattern, root).matches(path)).isTrue()
        }
    }

    @TestFactory
    fun `does not match the pattern in the absolute path without pattern but the relative path`() = gherkin {
        val pattern: String
        val root: Path
        val path: Path

        Given("a single regex pattern on Unix systems") {
            pattern = ".*/build/.*"
        }

        And("a root path with the pattern") {
            root = Paths.get("/tmp/detekt").toAbsolutePath()
        }

        When("we have a path that should match") {
            path = root.resolve("should/match/build/path")
        }

        Then("SinglePathFilter matches") {
            assertThat(SinglePathFilter(pattern, root).matches(path)).isTrue()
        }
    }
}

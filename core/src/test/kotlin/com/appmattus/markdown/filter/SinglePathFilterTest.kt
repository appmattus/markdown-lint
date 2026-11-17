package com.appmattus.markdown.filter

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.test.Test

class SinglePathFilterTest {
    @Test
    fun `invalid regex pattern throws an IllegalArgumentException`() {
        lateinit var pattern: String
        // Given an invalid regex pattern
        pattern = "*."

        // Then SinglePathFilter throws an IllegalArgumentException
        assertThatIllegalArgumentException().isThrownBy { SinglePathFilter(pattern) }
    }

    @Test
    fun `empty regex pattern throws an IllegalArgumentException`() {
        lateinit var pattern: String
        // Given an empty pattern
        pattern = ""

        // Then SinglePathFilter throws an IllegalArgumentException
        assertThatIllegalArgumentException().isThrownBy { SinglePathFilter(pattern) }
    }

    @Test
    fun `blank regex pattern throws an IllegalArgumentException`() {
        lateinit var pattern: String
        // Given a blank pattern
        pattern = "    "

        // Then SinglePathFilter throws an IllegalArgumentException
        assertThatIllegalArgumentException().isThrownBy { SinglePathFilter(pattern) }
    }

    @Test
    fun `matches relative path`() {
        lateinit var pattern: String
        lateinit var root: Path
        lateinit var path: Path

        // Given a single regex pattern on Unix systems
        pattern = ".*/build/.*"

        // And a default root path
        root = Paths.get("").toAbsolutePath()

        // When we have a relative path
        path = root.resolve("some/build/path/should/match")

        // Then SinglePathFilter matches
        assertThat(SinglePathFilter(pattern).matches(path)).isTrue()
    }

    @Test
    fun `matches relative path with the filter in the beginning`() {
        lateinit var pattern: String
        lateinit var root: Path
        lateinit var path: Path

        // Given a single regex pattern on Unix systems
        pattern = ".*/build/.*"

        // And a default root path
        root = Paths.get("").toAbsolutePath()

        // When we have a relative path with the filter in the beginning
        path = root.resolve("build/path/should/match")

        // Then SinglePathFilter matches
        assertThat(SinglePathFilter(pattern).matches(path)).isTrue()
    }

    @Test
    fun `matches relative path with the filter in the beginning v2`() {
        lateinit var pattern: String
        lateinit var root: Path
        lateinit var path: Path

        // Given a single regex pattern on Unix systems
        pattern = ".*/build/.*"

        // And a default root path
        root = Paths.get("").toAbsolutePath()

        // When we have a relative path with the filter in the beginning
        path = root.resolve("build/path/should/match")

        // Then SinglePathFilter matches
        assertThat(SinglePathFilter(pattern).matches(path)).isTrue()
    }

    @Test
    fun `does not match an unrelated path`() {
        lateinit var pattern: String
        lateinit var root: Path
        lateinit var path: Path

        // Given a single regex pattern on Unix systems
        pattern = ".*/build/.*"

        // And a default root path
        root = Paths.get("").toAbsolutePath()

        // When we have a path that should not match
        path = root.resolve("this/should/NOT/match")

        // Then SinglePathFilter does not match
        assertThat(SinglePathFilter(pattern).matches(path)).isFalse()
    }

    @Test
    fun `does not match the pattern in the root path`() {
        lateinit var pattern: String
        lateinit var root: Path
        lateinit var path: Path

        // Given a single regex pattern on Unix systems
        pattern = ".*/build/.*"

        // And a root path with the pattern
        root = Paths.get("/tmp/build/detekt").toAbsolutePath()

        // When we have a path that should not match
        path = root.resolve("should/not/match")

        // Then SinglePathFilter does not match
        assertThat(SinglePathFilter(pattern, root).matches(path)).isFalse()
    }

    @Test
    fun `does not match the pattern in the absolute path with pattern but the relative path`() {
        lateinit var pattern: String
        lateinit var root: Path
        lateinit var path: Path

        // Given a single regex pattern on Unix systems
        pattern = ".*/build/.*"

        // And a root path with the pattern
        root = Paths.get("/tmp/build/detekt").toAbsolutePath()

        // When we have a path that should match
        path = root.resolve("should/match/build/path")

        // Then SinglePathFilter matches
        assertThat(SinglePathFilter(pattern, root).matches(path)).isTrue()
    }

    @Test
    fun `does not match the pattern in the absolute path without pattern but the relative path`() {
        lateinit var pattern: String
        lateinit var root: Path
        lateinit var path: Path

        // Given a single regex pattern on Unix systems
        pattern = ".*/build/.*"

        // And a root path with the pattern
        root = Paths.get("/tmp/detekt").toAbsolutePath()

        // When we have a path that should match
        path = root.resolve("should/match/build/path")

        // Then SinglePathFilter matches
        assertThat(SinglePathFilter(pattern, root).matches(path)).isTrue()
    }
}

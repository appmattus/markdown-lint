package com.appmattus.markdown.filter

import java.nio.file.FileSystems
import java.nio.file.Path
import java.nio.file.PathMatcher
import java.nio.file.Paths
import java.util.regex.PatternSyntaxException

/**
 * Filter out files using the supplied regex [pattern] on paths relative to the [root] path.
 *
 * Respects both *nix and Windows paths.
 */
class SinglePathFilter(
    private val pattern: String, override val root: Path = Paths.get("").toAbsolutePath()
) : PathFilter {

    private val matcher: PathMatcher
    private val independentPattern: String

    init {
        if (pattern.isBlank()) {
            throw IllegalArgumentException("Empty patterns aren't acceptable")
        }

        try {
            FileSystems.getDefault().apply {
                independentPattern = pattern.replace("(?<!\\)/", Regex.escape(separator))
                matcher = getPathMatcher("regex:$independentPattern")
            }
        } catch (exception: PatternSyntaxException) {
            throw IllegalArgumentException("Provided regex is not valid: $pattern")
        }
    }

    override fun matches(path: Path): Boolean {
        val relativePath = Paths.get(".").resolve(root.relativize(path).normalize())
        return matcher.matches(relativePath)
    }
}

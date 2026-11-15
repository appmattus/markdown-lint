package com.appmattus.markdown.filter

import java.nio.file.Path
import java.nio.file.Paths

/**
 * Filter out files using the supplied regex [patterns] on paths relative to the [root] path.
 *
 * Respects both *nix and Windows paths.
 */
class MultiPathFilter(
    private val patterns: List<String>,
    override val root: Path = Paths.get("").toAbsolutePath()
) : PathFilter {

    private val filters = patterns.map {
        SinglePathFilter(it, root)
    }

    override fun matches(path: Path): Boolean {
        return filters.any { it.matches(path) }
    }
}

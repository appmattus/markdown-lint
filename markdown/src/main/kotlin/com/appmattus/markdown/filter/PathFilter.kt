package com.appmattus.markdown.filter

import java.nio.file.Path

interface PathFilter {
    val root: Path

    fun matches(path: Path): Boolean
}

package com.appmattus.markdown.rules.extentions

import com.vladsch.flexmark.util.sequence.BasedSequence
import com.vladsch.flexmark.util.sequence.BasedSequence.WHITESPACE_CHARS

fun BasedSequence.canTrim() =
    countLeading(WHITESPACE_CHARS, 0, length) + countTrailing(WHITESPACE_CHARS, 0, length) != 0

fun BasedSequence.splitIntoLines(): Array<BasedSequence> {
    var lastPos = 0
    val items = mutableListOf<BasedSequence>()

    while (lastPos < length) {
        val pos = endOfLineAnyEOL(lastPos)
        val eolLength = eolLength(pos)

        items += subSequence(lastPos, pos)
        lastPos = pos + eolLength
    }

    return items.toTypedArray()
}

// Required until https://github.com/vsch/flexmark-java/issues/362 is resolved
fun BasedSequence.indexOfAllList(s: CharSequence): List<Int> {
    val indices = mutableListOf<Int>()

    var currentIndex = 0

    while (true) {
        currentIndex = indexOf(s, currentIndex)

        if (currentIndex == -1) {
            return indices.toList()
        }

        indices.add(currentIndex)

        currentIndex += s.length
    }
}

package com.appmattus.markdown.rules.extensions

import com.vladsch.flexmark.util.misc.CharPredicate
import com.vladsch.flexmark.util.misc.CharPredicate.WHITESPACE
import com.vladsch.flexmark.util.sequence.BasedSequence

fun BasedSequence.canTrim() =
    countLeading(WHITESPACE, 0, length) + countTrailing(WHITESPACE, 0, length) != 0

fun BasedSequence.splitIntoLines(): Array<BasedSequence> {
    var lastPos = 0
    val items = mutableListOf<BasedSequence>()

    while (lastPos < length) {
        val pos = endOfLineAnyEOL(lastPos)
        val eolLength = eolStartLength(pos)

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

// flexmark's columnAtIndex returns an incorrect column for lines following a CRLF line ending
fun BasedSequence.lineColumnAt(index: Int): Int {
    val lineStart = lastIndexOfAny(CharPredicate.ANY_EOL, index)
    return index - if (lineStart == -1) 0 else lineStart + 1
}

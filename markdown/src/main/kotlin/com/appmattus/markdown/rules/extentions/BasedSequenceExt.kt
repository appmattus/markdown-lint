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

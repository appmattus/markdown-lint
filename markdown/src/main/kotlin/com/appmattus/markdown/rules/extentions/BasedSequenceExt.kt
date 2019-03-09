package com.appmattus.markdown.rules.extentions

import com.vladsch.flexmark.util.sequence.BasedSequence
import com.vladsch.flexmark.util.sequence.BasedSequence.WHITESPACE_CHARS

fun BasedSequence.canTrim() =
    countLeading(WHITESPACE_CHARS, 0, length) + countTrailing(WHITESPACE_CHARS, 0, length) != 0

fun BasedSequence.splitIntoLines() = split(listOf("\n", "\r", "\r\n"), 0, 0, "")

@Suppress("ComplexMethod", "NestedBlockDepth")
private fun BasedSequence.split(
    delimiter: List<String>,
    limit: Int,
    flags: Int,
    trimChars: String?
): Array<BasedSequence> {

    val trimCharsInternal = trimChars ?: WHITESPACE_CHARS
    val limitInternal = if (limit < 1) Integer.MAX_VALUE else limit

    val includeDelimiterParts = flags and BasedSequence.SPLIT_INCLUDE_DELIM_PARTS != 0
    val includeDelimiter = if (!includeDelimiterParts && flags and BasedSequence.SPLIT_INCLUDE_DELIMS != 0) 1 else 0
    val trimParts = flags and BasedSequence.SPLIT_TRIM_PARTS != 0
    val skipEmpty = flags and BasedSequence.SPLIT_SKIP_EMPTY != 0
    val items = mutableListOf<BasedSequence>()

    var lastPos = 0
    val length = length
    if (limitInternal > 1) {
        while (lastPos < length) {
            val pos = delimiter.map { indexOf(it, lastPos) }.filter { it >= 0 }.min() ?: break

            if (lastPos < pos || !skipEmpty) {
                var item = subSequence(lastPos, pos + includeDelimiter)
                if (trimParts) item = item.trim(trimCharsInternal)
                if (!item.isEmpty || !skipEmpty) {
                    items.add(item)
                    if (includeDelimiterParts) {
                        items.add(subSequence(pos, pos + 1))
                    }
                    if (items.size >= limitInternal - 1) {
                        lastPos = pos + 1
                        break
                    }
                }
            }
            lastPos = pos + 1
        }
    }

    if (lastPos < length) {
        var item = subSequence(lastPos, length)
        if (trimParts) item = item.trim(trimCharsInternal)
        if (!item.isEmpty || !skipEmpty) {
            items.add(item)
        }
    }
    return items.toTypedArray()
}

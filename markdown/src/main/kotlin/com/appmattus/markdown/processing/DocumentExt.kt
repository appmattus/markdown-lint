import com.vladsch.flexmark.util.Utils
import com.vladsch.flexmark.util.ast.Document
import com.vladsch.flexmark.util.sequence.BasedSequence

fun Document.getLineNumberFixed(offset: Int): Int {
    val lineSegments = contentLines
    if (lineSegments === BasedSequence.EMPTY_LIST) {
        val preText = chars.baseSubSequence(0, Utils.maxLimit(offset, chars.length))
        if (preText.isEmpty()) return 0
        var lineNumber = 0
        var nextLineEnd = preText.endOfLineAnyEOL(0)
        val length = preText.length
        while (nextLineEnd < length) {
            lineNumber++
            nextLineEnd = preText.endOfLineAnyEOL(nextLineEnd + preText.eolLength(nextLineEnd))
        }
        return lineNumber
    } else {
        val iMax = lineSegments.size
        for (i in 0 until iMax) {
            if (offset < lineSegments[i].endOffset) {
                return i
            }
        }
        return iMax
    }
}

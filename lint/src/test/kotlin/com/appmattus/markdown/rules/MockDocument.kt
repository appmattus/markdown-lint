import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.vladsch.flexmark.util.ast.Document
import com.vladsch.flexmark.util.sequence.BasedSequenceImpl

fun mockDocument() = mock<Document>().apply {
    whenever(chars).thenReturn(BasedSequenceImpl.of(""))
}

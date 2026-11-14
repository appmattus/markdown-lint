import com.vladsch.flexmark.util.ast.Document
import com.vladsch.flexmark.util.sequence.BasedSequence
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

fun mockDocument() = mock<Document>().apply {
    whenever(chars).thenReturn(BasedSequence.of(""))
}

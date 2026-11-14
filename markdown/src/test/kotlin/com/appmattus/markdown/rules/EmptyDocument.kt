import com.vladsch.flexmark.util.ast.Document
import com.vladsch.flexmark.util.data.MutableDataSet
import com.vladsch.flexmark.util.sequence.BasedSequence

fun emptyDocument() = Document(MutableDataSet(), BasedSequence.of(""))

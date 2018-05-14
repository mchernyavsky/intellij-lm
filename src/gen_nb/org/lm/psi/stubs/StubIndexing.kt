package org.lm.psi.stubs

import com.intellij.psi.stubs.IndexSink
import org.lm.psi.stubs.index.LmGotoClassIndex
import org.lm.psi.stubs.index.LmNamedElementIndex

private fun IndexSink.indexNamedStub(stubs: LmNamedStub) {
    stubs.name?.let { occurrence(LmNamedElementIndex.KEY, it) }
}

private fun IndexSink.indexDefinitionStub(stubs: LmNamedStub) {
    stubs.name?.let { occurrence(LmNamedElementIndex.KEY, it) }
}

private fun IndexSink.indexGotoClass(stubs: LmNamedStub) {
    stubs.name?.let { occurrence(LmGotoClassIndex.KEY, it) }
}

fun IndexSink.indexModule(stubs: LmModuleStub) {
    indexDefinitionStub(stubs)
    indexNamedStub(stubs)
    indexGotoClass(stubs)
}

fun IndexSink.indexVariable(stubs: LmVariableStub) {
    indexDefinitionStub(stubs)
    indexNamedStub(stubs)
}

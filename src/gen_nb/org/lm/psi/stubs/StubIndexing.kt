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
    indexNamedStub(stubs)
    indexDefinitionStub(stubs)
    indexGotoClass(stubs)
}

fun IndexSink.indexVariable(stubs: LmVariableStub) {
    indexNamedStub(stubs)
    indexDefinitionStub(stubs)
}

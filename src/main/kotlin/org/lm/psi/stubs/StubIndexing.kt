package org.lm.psi.stubs

import com.intellij.psi.stubs.IndexSink
import org.lm.psi.stubs.index.LmGotoClassIndex
import org.lm.psi.stubs.index.LmNamedElementIndex

fun IndexSink.indexModule(stub: LmModuleStub) {
    indexNamedStub(stub)
    indexDefinitionStub(stub)
    indexGotoClass(stub)
}

fun IndexSink.indexVariable(stub: LmVariableStub) {
    indexNamedStub(stub)
    indexDefinitionStub(stub)
}

fun IndexSink.indexFunction(stub: LmFunctionStub) {
    indexNamedStub(stub)
    indexDefinitionStub(stub)
}

private fun IndexSink.indexNamedStub(stub: LmNamedStub) {
    stub.name?.let { occurrence(LmNamedElementIndex.KEY, it) }
}

private fun IndexSink.indexDefinitionStub(stub: LmNamedStub) {
    stub.name?.let { occurrence(LmNamedElementIndex.KEY, it) }
}

private fun IndexSink.indexGotoClass(stub: LmNamedStub) {
    stub.name?.let { occurrence(LmGotoClassIndex.KEY, it) }
}

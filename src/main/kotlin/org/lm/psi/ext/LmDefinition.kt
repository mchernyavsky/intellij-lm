package org.lm.psi.ext

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import org.lm.psi.stubs.LmNamedStub

abstract class LmDefinitionMixin<StubT> : LmStubbedNamedElementImpl<StubT>
        where StubT : LmNamedStub, StubT : StubElement<*> {

    constructor(node: ASTNode) : super(node)

    constructor(stub: StubT, nodeType: IStubElementType<*, *>) : super(stub, nodeType)
}

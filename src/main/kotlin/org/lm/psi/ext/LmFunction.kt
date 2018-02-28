package org.lm.psi.ext

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import org.lm.psi.LmFunction
import org.lm.psi.stubs.LmFunctionStub

abstract class LmFunctionMixin : LmDefinitionMixin<LmFunctionStub>, LmFunction {

    constructor(node: ASTNode) : super(node)

    constructor(stub: LmFunctionStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)
}

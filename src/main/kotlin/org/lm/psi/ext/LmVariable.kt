package org.lm.psi.ext

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import org.lm.psi.LmVariable
import org.lm.psi.stubs.LmVariableStub

abstract class LmVariableMixin : LmDefinitionMixin<LmVariableStub>, LmVariable {
    constructor(node: ASTNode) : super(node)
    constructor(stub: LmVariableStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)
}

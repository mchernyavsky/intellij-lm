package org.lm.psi.ext

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import org.lm.psi.LmModule
import org.lm.psi.stubs.LmModuleStub

abstract class LmModuleMixin : LmDefinitionMixin<LmModuleStub>, LmModule {
    constructor(node: ASTNode) : super(node)

    constructor(node: LmModuleStub, nodeType: IStubElementType<*, *>) : super(node, nodeType)
}

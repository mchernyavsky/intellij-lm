package org.lm.psi.ext

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import org.lm.psi.LmModule
import org.lm.psi.stubs.LmModuleStub
import org.lm.resolve.NamespaceProvider
import org.lm.resolve.namespace.Namespace

abstract class LmModuleMixin : LmDefinitionMixin<LmModuleStub>, LmModule {
    override val namespace: Namespace = NamespaceProvider.forModule(this)

    constructor(node: ASTNode) : super(node)

    constructor(stub: LmModuleStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)
}
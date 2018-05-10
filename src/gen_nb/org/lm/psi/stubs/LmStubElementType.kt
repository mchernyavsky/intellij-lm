package org.lm.psi.stubs

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.tree.IStubFileElementType
import kotlin.Boolean
import kotlin.String
import org.lm.LmLanguage
import org.lm.psi.ext.LmElement

abstract class LmStubElementType<StubT : StubElement<*>, PsiT : LmElement>(debugName: String) : IStubElementType<StubT, PsiT>(debugName, LmLanguage) {
    final override fun getExternalId(): String = "org.lm.${super.toString()}"

    protected fun createStubIfParentIsStub(node: ASTNode): Boolean {
        val parent = node.treeParent
        val parentType = parent.elementType
        return (parentType is IStubElementType<*, *> && parentType.shouldCreateStub(parent)) || parentType is IStubFileElementType<*>
    }
}

package org.lm.psi.ext

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import org.lm.resolve.LmReference
import org.lm.resolve.NamespaceScope
import org.lm.resolve.OverridingScope
import org.lm.resolve.Scope
import org.lm.resolve.namespace.EmptyNamespace
import org.lm.resolve.namespace.Namespace

interface LmCompositeElement : PsiElement {
    val namespace: Namespace
    val scope: Scope

    override fun getReference(): LmReference?
}

abstract class LmCompositeElementImpl(node: ASTNode) : ASTWrapperPsiElement(node),
                                                       LmCompositeElement {
    override val namespace: Namespace = EmptyNamespace

    override val scope: Scope
        get() {
            val parentScope = (parent as? LmCompositeElement)?.scope
            val namespaceScope = NamespaceScope(namespace)
            parentScope?.let { return OverridingScope(it, namespaceScope) }
            return namespaceScope
        }

    override fun getReference(): LmReference? = null
}

abstract class LmStubbedElementImpl<StubT : StubElement<*>> : StubBasedPsiElementBase<StubT>,
                                                              LmCompositeElement {
    override val namespace: Namespace = EmptyNamespace

    override val scope: Scope
        get() {
            val parentScope = (parent as? LmCompositeElement)?.scope
            val namespaceScope = NamespaceScope(namespace)
            parentScope?.let { return OverridingScope(it, namespaceScope) }
            return namespaceScope
        }

    constructor(node: ASTNode) : super(node)

    constructor(stub: StubT, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun toString(): String = "${javaClass.simpleName}($elementType)"

    override fun getReference(): LmReference? = null
}

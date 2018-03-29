package org.lm.psi.ext

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import org.lm.resolve.*

interface LmElement : PsiElement {
    val namespace: Scope
    val lexicalScope: Scope
    val scope: Scope

    override fun getReference(): LmReference?
}

abstract class LmElementImpl(node: ASTNode) : ASTWrapperPsiElement(node), LmElement {
    override val namespace: Scope = EmptyScope

    override val lexicalScope: Scope
        get() {
            val localScope = ScopeProvider.forElement(this)
            val parentScope = (parent as? LmElement)?.lexicalScope ?: return localScope
            return when {
                localScope.items.isEmpty() -> parentScope
                parentScope.items.isEmpty() -> localScope
                else -> OverridingScope(parentScope, localScope)
            }
        }

    override val scope: Scope
        get() {
            val localScope = ScopeProvider.forElement(this, isLexical = false)
            val parentScope = (parent as? LmElement)?.scope ?: return localScope
            return when {
                localScope.items.isEmpty() -> parentScope
                parentScope.items.isEmpty() -> localScope
                else -> OverridingScope(parentScope, localScope)
            }
        }

    override fun getReference(): LmReference? = null
}

abstract class LmStubbedElementImpl<StubT : StubElement<*>> : StubBasedPsiElementBase<StubT>, LmElement {
    override val namespace: Scope = EmptyScope

    override val lexicalScope: Scope
        get() {
            val localScope = ScopeProvider.forElement(this)
            val parentScope = (parent as? LmElement)?.lexicalScope ?: return localScope
            return when {
                localScope.items.isEmpty() -> parentScope
                parentScope.items.isEmpty() -> localScope
                else -> OverridingScope(parentScope, localScope)
            }
        }

    override val scope: Scope
        get() {
            val localScope = ScopeProvider.forElement(this, isLexical = false)
            val parentScope = (parent as? LmElement)?.scope ?: return localScope
            return when {
                localScope.items.isEmpty() -> parentScope
                parentScope.items.isEmpty() -> localScope
                else -> OverridingScope(parentScope, localScope)
            }
        }

    constructor(node: ASTNode) : super(node)

    constructor(stub: StubT, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun toString(): String = "${javaClass.simpleName}($elementType)"

    override fun getReference(): LmReference? = null
}

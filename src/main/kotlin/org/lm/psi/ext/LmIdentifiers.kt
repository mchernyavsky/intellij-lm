package org.lm.psi.ext

import com.intellij.lang.ASTNode
import org.lm.psi.LmDefinitionId
import org.lm.psi.LmQualifiedIdPart
import org.lm.resolve.LmReference
import org.lm.resolve.LmReferenceBase
import org.lm.resolve.Scope
import org.lm.resolve.ScopeProvider

abstract class LmDefinitionIdImplMixin(node: ASTNode) : LmElementImpl(node), LmDefinitionId {

    override val referenceNameElement: LmDefinitionIdImplMixin
        get() = this

    override val referenceName: String
        get() = referenceNameElement.text

    override fun getName(): String = referenceName
}

abstract class LmQualifiedIdPartImplMixin(node: ASTNode) : LmElementImpl(node), LmQualifiedIdPart {
    val scope: Scope
        get() = ScopeProvider.getScope(this)

    override val referenceNameElement: LmElement
        get() = this

    override val referenceName: String
        get() = text

    override fun getName(): String = referenceName

    override fun getReference(): LmReference = LmIdReference()

    private inner class LmIdReference : LmReferenceBase<LmQualifiedIdPart>(this@LmQualifiedIdPartImplMixin) {

        override fun resolve(): LmElement? = scope.resolve(name)

        override fun getVariants(): Array<Any> = scope.symbols.toTypedArray()
    }
}

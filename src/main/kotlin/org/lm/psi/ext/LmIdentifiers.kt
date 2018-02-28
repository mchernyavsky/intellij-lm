package org.lm.psi.ext

import com.intellij.lang.ASTNode
import org.lm.psi.LmDefinitionId
import org.lm.psi.LmQualifiedId
import org.lm.resolve.EmptyScope
import org.lm.resolve.LmReference
import org.lm.resolve.LmReferenceBase
import org.lm.resolve.Scope
import org.lm.resolve.namespace.EmptyNamespace
import org.lm.resolve.namespace.Namespace

abstract class LmDefinitionIdImplMixin(node: ASTNode): LmCompositeElementImpl(node),
    LmDefinitionId {

    override val referenceNameElement: LmDefinitionIdImplMixin
        get() = this

    override val referenceName: String
        get() = referenceNameElement.text

    override fun getName(): String = referenceName
}

abstract class LmQualifiedIdImplMixin(node: ASTNode) : LmCompositeElementImpl(node),
    LmQualifiedId {
    override val namespace: Namespace
        get() {
            val resolved = reference.resolve() as? LmCompositeElement
            return resolved?.namespace ?: EmptyNamespace
        }

    override val scope: Scope
        get() = (parent as? LmCompositeElement)?.scope ?: EmptyScope

    override val referenceNameElement: LmCompositeElement
        get() = this

    override val referenceName: String
        get() = text

    override fun getName(): String = referenceName

    override fun getReference(): LmReference = VcPrefixReference()

    private inner class VcPrefixReference : LmReferenceBase<LmQualifiedId>(this@LmQualifiedIdImplMixin) {

        override fun resolve(): LmCompositeElement? = scope.resolve(name)

        override fun getVariants(): Array<Any> = scope.symbols.toTypedArray()
    }
}

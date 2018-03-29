package org.lm.psi.ext

import com.intellij.lang.ASTNode
import org.lm.psi.LmDefinitionId
import org.lm.psi.LmQualifiedIdPart
import org.lm.psi.prevSiblingOfType
import org.lm.resolve.EmptyScope
import org.lm.resolve.LmReference
import org.lm.resolve.LmReferenceBase
import org.lm.resolve.Scope

abstract class LmDefinitionIdImplMixin(node: ASTNode) : LmElementImpl(node), LmDefinitionId {

    override val referenceNameElement: LmDefinitionIdImplMixin
        get() = this

    override val referenceName: String
        get() = referenceNameElement.text

    override fun getName(): String = referenceName
}

abstract class LmQualifiedIdPartImplMixin(node: ASTNode) : LmElementImpl(node), LmQualifiedIdPart {
    override val namespace: Scope
        get() {
            val resolved = reference.resolve() as? LmElement
            return resolved?.namespace ?: EmptyScope
        }

    override val referenceNameElement: LmElement
        get() = this

    override val referenceName: String
        get() = text

    override fun getName(): String = referenceName

    override fun getReference(): LmReference = LmIdReference()

    private inner class LmIdReference : LmReferenceBase<LmQualifiedIdPart>(this@LmQualifiedIdPartImplMixin) {

        override fun resolve(): LmElement? {
            val parent = prevSiblingOfType<LmQualifiedIdPart>() ?: return scope.resolve(name)
            return parent.namespace.resolve(name)
        }

        override fun getVariants(): Array<Any> {
            val parent = prevSiblingOfType<LmQualifiedIdPart>() ?: return scope.symbols.toTypedArray()
            return parent.namespace.symbols.toTypedArray()
        }
    }
}

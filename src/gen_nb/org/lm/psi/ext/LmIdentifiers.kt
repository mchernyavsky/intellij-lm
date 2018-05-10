package org.lm.psi.ext

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import kotlin.Any
import kotlin.Array
import kotlin.String
import org.lm.psi.LmDefinitionId
import org.lm.psi.LmQualifiedIdPart
import org.lm.resolve.LmReference
import org.lm.resolve.LmReferenceBase
import org.lm.resolve.ScopeProvider
import org.nbkit.common.resolve.Scope

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
        override fun resolve(): PsiElement? = scope.resolve(name)

        override fun getVariants(): Array<Any> = scope.symbols.toTypedArray()
    }
}

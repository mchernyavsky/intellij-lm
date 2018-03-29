package org.lm.psi.ext

import com.intellij.lang.ASTNode
import org.lm.psi.LmBinding
import org.lm.resolve.OverridingScope
import org.lm.resolve.Scope
import org.lm.resolve.ScopeProvider

abstract class LmBindingMixinImpl(node: ASTNode) : LmNamedElementImpl(node), LmBinding {

    override val lexicalScope: Scope
        get() {
            val localScope = ScopeProvider.forElement(this)
            val parentScope = (parent.parent as? LmElement)?.lexicalScope ?: return localScope
            return when {
                localScope.items.isEmpty() -> parentScope
                parentScope.items.isEmpty() -> localScope
                else -> OverridingScope(parentScope, localScope)
            }
        }

    override val scope: Scope
        get() {
            val localScope = ScopeProvider.forElement(this, isLexical = false)
            val parentScope = (parent.parent as? LmElement)?.scope ?: return localScope
            return when {
                localScope.items.isEmpty() -> parentScope
                parentScope.items.isEmpty() -> localScope
                else -> OverridingScope(parentScope, localScope)
            }
        }
}

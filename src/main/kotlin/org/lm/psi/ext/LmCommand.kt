package org.lm.psi.ext

import com.intellij.lang.ASTNode
import org.lm.psi.LmCommand
import org.lm.resolve.Scope

abstract class LmCommandImplMixin(node: ASTNode) : LmElementImpl(node), LmCommand {
    override val scope: Scope
        get() = lexicalScope
}

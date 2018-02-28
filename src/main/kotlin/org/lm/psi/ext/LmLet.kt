package org.lm.psi.ext

import com.intellij.lang.ASTNode
import org.lm.psi.LmLet
import org.lm.resolve.EmptyScope
import org.lm.resolve.Scope
import org.lm.resolve.namespace.EmptyNamespace
import org.lm.resolve.namespace.Namespace

abstract class LmLetImplMixin(node: ASTNode) : LmCompositeElementImpl(node), LmLet {

    override val namespace: Namespace = EmptyNamespace

    override val scope: Scope = EmptyScope
}

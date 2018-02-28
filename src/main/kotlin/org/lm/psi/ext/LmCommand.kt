package org.lm.psi.ext

import com.intellij.lang.ASTNode
import org.lm.psi.LmCommand
import org.lm.resolve.EmptyScope
import org.lm.resolve.Scope
import org.lm.resolve.namespace.EmptyNamespace
import org.lm.resolve.namespace.Namespace

abstract class LmCommandImplMixin(node: ASTNode) : LmCompositeElementImpl(node), LmCommand {

    override val namespace: Namespace = EmptyNamespace

    override val scope: Scope = EmptyScope
}

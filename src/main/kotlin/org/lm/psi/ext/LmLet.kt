package org.lm.psi.ext

import com.intellij.lang.ASTNode
import org.lm.psi.LmLet

abstract class LmLetImplMixin(node: ASTNode) : LmElementImpl(node), LmLet


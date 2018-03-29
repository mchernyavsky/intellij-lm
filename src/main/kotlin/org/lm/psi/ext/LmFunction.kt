package org.lm.psi.ext

import com.intellij.lang.ASTNode
import org.lm.psi.LmFunction

abstract class LmFunctionImplMixin(node: ASTNode) : LmElementImpl(node), LmFunction

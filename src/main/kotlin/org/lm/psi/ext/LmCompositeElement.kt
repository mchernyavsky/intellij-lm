package org.lm.psi.ext

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

interface LmCompositeElement : PsiElement

abstract class LmCompositeElementImpl(
        node: ASTNode
) : ASTWrapperPsiElement(node), LmCompositeElement

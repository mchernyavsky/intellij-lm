package org.lm.psi.ext

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import kotlin.String
import org.lm.resolve.LmReference

interface LmElement : PsiElement {
    override fun getReference(): LmReference?
}

abstract class LmElementImpl(node: ASTNode) : ASTWrapperPsiElement(node), LmElement {
    override fun getReference(): LmReference? = null
}

abstract class LmStubbedElementImpl<StubT : StubElement<*>> : StubBasedPsiElementBase<StubT>,
        LmElement {
    constructor(node: ASTNode) : super(node)

    constructor(node: StubT, nodeType: IStubElementType<*, *>) : super(node, nodeType)

    override fun toString(): String = "${javaClass.simpleName}($elementType)"

    override fun getReference(): LmReference? = null
}

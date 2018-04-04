package org.lm.psi.ext

import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import org.lm.navigation.getPresentation
import org.lm.psi.LmDefinitionId
import org.lm.psi.LmPsiFactory
import org.lm.psi.stubs.LmNamedStub

interface LmNamedElement : LmElement, PsiNameIdentifierOwner, NavigatablePsiElement

abstract class LmNamedElementImpl(node: ASTNode) : LmElementImpl(node), LmNamedElement {

    override fun getNameIdentifier(): LmElementImpl? = childOfType()

    override fun getName(): String? = nameIdentifier?.text

    override fun setName(name: String): PsiElement? {
        nameIdentifier?.replace(LmPsiFactory(project).createDefinitionId(name))
        return this
    }

    override fun getNavigationElement(): PsiElement = nameIdentifier ?: this

    override fun getTextOffset(): Int = nameIdentifier?.textOffset ?: super.getTextOffset()

    override fun getPresentation(): ItemPresentation = getPresentation(this)
}

abstract class LmStubbedNamedElementImpl<StubT> : LmStubbedElementImpl<StubT>, LmNamedElement
        where StubT : LmNamedStub, StubT : StubElement<*> {

    constructor(node: ASTNode) : super(node)

    constructor(stub: StubT, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun getNameIdentifier(): LmDefinitionId? = childOfType()

    override fun getName(): String? = nameIdentifier?.text

    override fun setName(name: String): PsiElement? {
        nameIdentifier?.replace(LmPsiFactory(project).createDefinitionId(name))
        return this
    }

    override fun getNavigationElement(): PsiElement = nameIdentifier ?: this

    override fun getTextOffset(): Int = nameIdentifier?.textOffset ?: super.getTextOffset()

    override fun getPresentation(): ItemPresentation = getPresentation(this)
}

package org.lm.psi.ext

import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.util.PsiTreeUtil
import kotlin.Int
import kotlin.String
import org.lm.navigation.getPresentation
import org.lm.psi.LmDefinitionId
import org.lm.psi.LmPsiFactory
import org.lm.psi.LmQualifiedIdPart
import org.lm.psi.stubs.LmNamedStub

interface LmNamedElement : LmElement, PsiNameIdentifierOwner, NavigatablePsiElement

abstract class LmNamedElementImpl(node: ASTNode) : LmElementImpl(node), LmNamedElement {
    override fun getNameIdentifier(): LmElement? = PsiTreeUtil.findChildOfAnyType(this, true, LmDefinitionId::class.java, LmQualifiedIdPart::class.java)

    override fun getName(): String? = nameIdentifier?.text

    override fun setName(name: String): PsiElement? {
        val factory = LmPsiFactory(project)
        val newNameIdentifier = when (nameIdentifier) {
            is LmDefinitionId -> factory.createDefinitionId(name)
            is LmQualifiedIdPart -> factory.createQualifiedIdPart(name)
            else -> return this
        }
        nameIdentifier?.replace(newNameIdentifier)
        return this
    }

    override fun getNavigationElement(): PsiElement = nameIdentifier ?: this

    override fun getTextOffset(): Int = nameIdentifier?.textOffset ?: super.getTextOffset()

    override fun getPresentation(): ItemPresentation = getPresentation(this)
}

abstract class LmStubbedNamedElementImpl<StubT> : LmStubbedElementImpl<StubT>,
        LmNamedElement where StubT : LmNamedStub, StubT : StubElement<*> {
    constructor(node: ASTNode) : super(node)

    constructor(node: StubT, nodeType: IStubElementType<*, *>) : super(node, nodeType)

    override fun getNameIdentifier(): LmElement? = PsiTreeUtil.findChildOfAnyType(this, true, LmDefinitionId::class.java, LmQualifiedIdPart::class.java)

    override fun getName(): String? = nameIdentifier?.text

    override fun setName(name: String): PsiElement? {
        val factory = LmPsiFactory(project)
        val newNameIdentifier = when (nameIdentifier) {
            is LmDefinitionId -> factory.createDefinitionId(name)
            is LmQualifiedIdPart -> factory.createQualifiedIdPart(name)
            else -> return this
        }

        nameIdentifier?.replace(newNameIdentifier)
        return this
    }

    override fun getNavigationElement(): PsiElement = nameIdentifier ?: this

    override fun getTextOffset(): Int = nameIdentifier?.textOffset ?: super.getTextOffset()

    override fun getPresentation(): ItemPresentation = getPresentation(this)
}

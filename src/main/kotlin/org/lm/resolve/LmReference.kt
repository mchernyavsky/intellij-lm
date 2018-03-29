package org.lm.resolve

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceBase
import org.lm.LmFileType
import org.lm.psi.LmDefinitionId
import org.lm.psi.LmPsiFactory
import org.lm.psi.LmQualifiedIdPart
import org.lm.psi.ext.LmElement
import org.lm.psi.ext.LmReferenceElement
import org.lm.refactoring.LmNamesValidator

interface LmReference : PsiReference {

    override fun getElement(): LmElement

    override fun resolve(): PsiElement?
}

abstract class LmReferenceBase<T : LmReferenceElement>(element: T)
    : PsiReferenceBase<T>(element, TextRange(0, element.textLength)), LmReference {

    override fun handleElementRename(newName: String): PsiElement {
        element.referenceNameElement?.let { doRename(it, newName) }
        return element
    }

    companion object {
        private fun doRename(oldNameIdentifier: PsiElement, rawName: String) {
            val name = rawName.removeSuffix('.' + LmFileType.defaultExtension)
            if (!LmNamesValidator().isIdentifier(name, oldNameIdentifier.project)) return
            val factory = LmPsiFactory(oldNameIdentifier.project)
            val newNameIdentifier = when (oldNameIdentifier) {
                is LmDefinitionId -> factory.createDefinitionId(name)
                is LmQualifiedIdPart -> factory.createQualifiedIdPart(name)
                else -> return
            }
            oldNameIdentifier.replace(newNameIdentifier)
        }
    }
}

package org.lm.psi

import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import org.lm.psi.ext.LmNamedElement
import org.lm.resolve.LmReference
import org.lm.resolve.NamespaceProvider
import org.lm.resolve.Scope

class LmDirectory(directory: PsiDirectory) : PsiDirectory by directory, LmNamedElement {
    override fun getReference(): LmReference? = null
    override fun getNameIdentifier(): PsiElement? = null
}

package org.lm.psi

import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import org.lm.psi.ext.LmNamedElement
import org.lm.resolve.LmReference

class LmDirectoryWrapper(directory: PsiDirectory) : LmNamedElement, PsiDirectory by directory {
    override fun getReference(): LmReference? = null

    override fun getNameIdentifier(): PsiElement? = null
}

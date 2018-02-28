package org.lm.refactoring

import com.intellij.lang.refactoring.RefactoringSupportProvider
import com.intellij.psi.PsiElement
import org.lm.psi.ext.LmNamedElement

class LmRefactoringSupportProvider : RefactoringSupportProvider() {
    override fun isSafeDeleteAvailable(element: PsiElement): Boolean {
        return element is LmNamedElement
    }
}

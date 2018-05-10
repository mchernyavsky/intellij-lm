package org.lm.search

import com.intellij.lang.HelpID
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import kotlin.Boolean
import kotlin.String
import org.lm.psi.LmModule
import org.lm.psi.LmVariable
import org.lm.psi.ext.LmNamedElement

class LmFindUsagesProvider : FindUsagesProvider {
    override fun getWordsScanner(): WordsScanner = LmWordScanner()

    override fun canFindUsagesFor(element: PsiElement): Boolean = element is LmNamedElement

    override fun getHelpId(element: PsiElement): String = HelpID.FIND_OTHER_USAGES

    override fun getType(element: PsiElement): String = when (element) {
        is LmModule -> "module"
        is LmVariable -> "variable"
        else -> ""
    }

    override fun getDescriptiveName(element: PsiElement): String = when (element) {
        is LmNamedElement -> element.name ?: "<unnamed>"
        else -> ""
    }

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String = when (element) {
        is LmNamedElement -> element.name!!
        else -> ""
    }
}

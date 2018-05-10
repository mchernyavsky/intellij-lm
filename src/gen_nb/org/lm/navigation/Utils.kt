package org.lm.navigation

import com.intellij.ide.projectView.PresentationData
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import kotlin.String
import org.lm.psi.ext.LmElement
import org.lm.psi.ext.LmNamedElement

fun getPresentation(psi: LmElement): ItemPresentation {
    val location = "(in ${psi.containingFile.name})"
    val name = presentableName(psi)
    return PresentationData(name, location, psi.getIcon(0), null)
}

private fun presentableName(psi: PsiElement): String? = when (psi) {
    is LmNamedElement -> psi.name
    else -> null
}

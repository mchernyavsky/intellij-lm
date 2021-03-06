package org.lm.navigation

import com.intellij.ide.projectView.PresentationData
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import org.lm.psi.ext.LmElement
import org.lm.psi.ext.LmNamedElement
import org.lm.psi.ext.fullName
import org.lm.psi.ext.module

fun getPresentation(psi: LmElement): ItemPresentation {
    val location = run {
        val module = psi.module
        "(in ${module?.fullName ?: psi.containingFile.name})"
    }

    val name = presentableName(psi)
    return PresentationData(name, location, psi.getIcon(0), null)
}

fun getPresentationForStructure(psi: LmElement): ItemPresentation =
        PresentationData(presentableName(psi), null, psi.getIcon(0), null)

private fun presentableName(psi: PsiElement): String? = when (psi) {
    is LmNamedElement -> psi.name
    else -> null
}

package org.lm.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

val PsiElement.ancestors: Sequence<PsiElement>
    get() = generateSequence(this) { it.parent }

val PsiElement.module: LmModule?
    get() = ancestors.filterIsInstance<LmModule>().firstOrNull()

val LmModule.fullName: String
    get() {
        val ancestors = ancestors.toMutableList()
        ancestors.reverse()
        return ancestors
            .filterIsInstance<LmModule>()
            .joinToString(".") { it.name!! }
    }

inline fun <reified T : PsiElement> PsiElement.parentOfType(
    strict: Boolean = true,
    minStartOffset: Int = -1
): T? = PsiTreeUtil.getParentOfType(this, T::class.java, strict, minStartOffset)

inline fun <reified T : PsiElement> PsiElement.childOfType(
    strict: Boolean = true
): T? = PsiTreeUtil.findChildOfType(this, T::class.java, strict)

inline fun <reified T : PsiElement> PsiElement.prevSiblingOfType(): T? =
        PsiTreeUtil.getPrevSiblingOfType(this, T::class.java)

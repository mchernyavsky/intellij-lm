package org.lm.psi.ext

import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.PsiUtilCore
import org.lm.psi.LmFile
import org.lm.psi.LmModule
import org.lm.psi.stubs.LmFileStub

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

val PsiElement.elementType: IElementType
    get() = if (this is LmFile) LmFileStub.Type else PsiUtilCore.getElementType(this)

inline fun <reified T : PsiElement> PsiElement.ancestorOrSelf(): T? =
        PsiTreeUtil.getParentOfType(this, T::class.java, false)

inline fun <reified T : PsiElement> PsiElement.descendantsOfType(): Collection<T> =
        PsiTreeUtil.findChildrenOfType(this, T::class.java)

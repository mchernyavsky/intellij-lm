package org.lm.psi.ext

import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.PsiUtilCore
import org.lm.psi.LmFile
import org.lm.psi.stubs.LmFileStub

val PsiElement.ancestors: Sequence<PsiElement>
    get() = generateSequence(this) { if (it is PsiFile) null else it.parent }

val PsiElement.ancestorPairs: Sequence<Pair<PsiElement, PsiElement>>
    get() {
        val parent = this.parent ?: return emptySequence()
        return generateSequence(Pair(this, parent)) { (_, parent) ->
            val grandPa = parent.parent
            if (parent is PsiFile || grandPa == null) null else parent to grandPa
        }
    }

val PsiElement.elementType: IElementType
    get() = if (this is LmFile) LmFileStub.Type else PsiUtilCore.getElementType(this)


inline fun <reified T : PsiElement> PsiElement.ancestorStrict(): T? =
        PsiTreeUtil.getParentOfType(this, T::class.java, true)

inline fun <reified T : PsiElement> PsiElement.ancestorOrSelf(): T? =
        PsiTreeUtil.getParentOfType(this, T::class.java, false)

inline fun <reified T : PsiElement> PsiElement.ancestorOrSelf(stopAt: Class<out PsiElement>): T? =
        PsiTreeUtil.getParentOfType(this, T::class.java, false, stopAt)

inline fun <reified T : PsiElement> PsiElement.contextStrict(): T? =
        PsiTreeUtil.getContextOfType(this, T::class.java, true)

inline fun <reified T : PsiElement> PsiElement.contextOrSelf(): T? =
        PsiTreeUtil.getContextOfType(this, T::class.java, false)

inline fun <reified T : PsiElement> PsiElement.childrenOfType(): List<T> =
        PsiTreeUtil.getChildrenOfTypeAsList(this, T::class.java)

inline fun <reified T : PsiElement> PsiElement.stubChildrenOfType(): List<T> =
        PsiTreeUtil.getStubChildrenOfTypeAsList(this, T::class.java)

inline fun <reified T : PsiElement> PsiElement.descendantOfTypeStrict(): T? =
        PsiTreeUtil.findChildOfType(this, T::class.java, true)

inline fun <reified T : PsiElement> PsiElement.descendantsOfType(): Collection<T> =
        PsiTreeUtil.findChildrenOfType(this, T::class.java)

val PsiElement.contextualFile: PsiFile
    get() = contextOrSelf() ?: error("Element outside of file: $text")

fun PsiElement?.getPrevNonCommentSibling(): PsiElement? =
        PsiTreeUtil.skipSiblingsBackward(this, PsiWhiteSpace::class.java, PsiComment::class.java)

fun PsiElement?.getNextNonCommentSibling(): PsiElement? =
        PsiTreeUtil.skipSiblingsForward(this, PsiWhiteSpace::class.java, PsiComment::class.java)

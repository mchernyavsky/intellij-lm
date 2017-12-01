package org.lm.annotation

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement
import org.lm.highlight.LmHighlightingColors
import org.lm.psi.LmDid

class LmHighlightingAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        val color = if (element is LmDid) LmHighlightingColors.DECLARATION else return
        holder.createInfoAnnotation(element, null).textAttributes = color.textAttributesKey
    }
}

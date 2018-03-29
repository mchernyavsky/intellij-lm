package org.lm.resolve

import com.intellij.psi.PsiElement
import org.intellij.lang.annotations.Language
import org.lm.LmTestBase
import org.lm.psi.ext.LmNamedElement
import org.lm.psi.ext.LmReferenceElement

abstract class LmResolveTestBase : LmTestBase() {
    open protected fun checkByCode(@Language("LM") code: String) {
        InlineFile(code)

        val (refElement, data) = findElementAndDataInEditor<LmReferenceElement>("^")

        if (data == "unresolved") {
            val resolved = refElement.reference?.resolve()
            check(resolved == null) {
                "$refElement `${refElement.text}`should be unresolved, was resolved to\n$resolved `${resolved?.text}`"
            }
            return
        }

        val resolved = refElement.checkedResolve()
        val target = findElementInEditor<LmNamedElement>("X")

        check(resolved == target) {
            "$refElement `${refElement.text}` should resolve to $target, was $resolved instead"
        }
    }
}

private fun LmReferenceElement.checkedResolve(): PsiElement {
    return reference?.resolve() ?: error("Failed to resolve $text")
}

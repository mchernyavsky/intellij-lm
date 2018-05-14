package org.lm.resolve

import com.intellij.psi.PsiElement
import kotlin.Any
import kotlin.Boolean
import org.lm.psi.LmBinding
import org.lm.psi.LmDefinition
import org.lm.psi.LmDirectoryWrapper
import org.lm.psi.LmExpression
import org.lm.psi.LmFile
import org.lm.psi.LmFileWrapper
import org.lm.psi.LmFunction
import org.lm.psi.LmLetPar
import org.lm.psi.LmLetRec
import org.lm.psi.LmLetSeq
import org.lm.psi.LmModule
import org.lm.psi.LmParameter
import org.lm.psi.LmQualifiedIdPart
import org.lm.psi.ext.LmQualifiedIdPartImplMixin
import org.nbkit.common.psi.ext.childrenOfType
import org.nbkit.common.psi.ext.prevSiblingOfType
import org.nbkit.common.resolve.EmptyScope
import org.nbkit.common.resolve.LocalScope
import org.nbkit.common.resolve.OverridingScope
import org.nbkit.common.resolve.Scope

object ScopeProvider {
    fun getScope(element: LmQualifiedIdPart): Scope = forIdentifier(element.prevSiblingOfType<LmQualifiedIdPartImplMixin>(), element)

    private fun forIdentifier(prevSibling: LmQualifiedIdPartImplMixin?, element: PsiElement): Scope = if (prevSibling != null) {
        val resolved = prevSibling.scope.resolve(prevSibling.name)
        when (resolved) {
            is LmModule -> NamespaceProvider.forModule(resolved, true)
            is LmFileWrapper -> NamespaceProvider.forFile(resolved, true)
            is LmDirectoryWrapper -> NamespaceProvider.forDirectory(resolved)
            else -> EmptyScope
        }
    } else {
        forElement(element.parent, element)
    }

    private fun forElement(
            element: PsiElement?,
            prev: PsiElement,
            useCommand: Boolean = true
    ): Scope {
        element ?: return LocalScope()
        val parentScope = forElement(element.parent, element)
        val scope = when (element) {
            is LmFunction -> forFunction(element, prev)
            is LmLetSeq -> forLetSeq(element, prev)
            is LmLetRec -> forLetRec(element, prev)
            is LmLetPar -> forLetPar(element, prev)
            is LmModule -> forModule(element, prev)
            is LmFile -> forFile(element, prev)
            else -> EmptyScope
        }
        return when {
            parentScope.items.isEmpty() -> scope
            scope.items.isEmpty() -> parentScope
            else -> OverridingScope(parentScope, scope)
        }
    }

    private fun forFunction(element: LmFunction, prev: PsiElement): Scope {
        val scope = LocalScope()
        if (prev is Any) {
            element
                .childrenOfType<LmParameter>(null)
                .forEach { scope.put(it) }
        }
        return scope
    }

    private fun forLetSeq(element: LmLetSeq, prev: PsiElement): Scope {
        val scope = LocalScope()
        if (prev is LmExpression) {
            element
                .childrenOfType<LmBinding>(null)
                .forEach { scope.put(it) }
        }
        if (prev is LmBinding) {
            element
                .childrenOfType<LmBinding>(prev)
                .forEach { scope.put(it) }
        }
        return scope
    }

    private fun forLetRec(element: LmLetRec, prev: PsiElement): Scope {
        val scope = LocalScope()
        if (prev is Any) {
            element
                .childrenOfType<LmBinding>(null)
                .forEach { scope.put(it) }
        }
        return scope
    }

    private fun forLetPar(element: LmLetPar, prev: PsiElement): Scope {
        val scope = LocalScope()
        if (prev is LmExpression) {
            element
                .childrenOfType<LmBinding>(null)
                .forEach { scope.put(it) }
        }
        return scope
    }

    private fun forModule(element: LmModule, prev: PsiElement): Scope {
        val scope = LocalScope()
        if (prev is LmDefinition) {
            element
                .childrenOfType<LmDefinition>(prev)
                .forEach { scope.put(it) }
        }
        return scope
    }

    private fun forFile(element: LmFile, prev: PsiElement): Scope {
        val scope = LocalScope()
        if (prev is LmDefinition) {
            element
                .childrenOfType<LmDefinition>(prev)
                .forEach { scope.put(it) }
        }
        return scope
    }
}

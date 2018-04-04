package org.lm.resolve

import com.intellij.psi.PsiElement
import org.lm.psi.*
import org.lm.psi.ext.LmQualifiedIdPartImplMixin
import org.lm.psi.ext.elementType

object ScopeProvider {

    fun getScope(id: LmQualifiedIdPart): Scope {
        val prev = id.prevSiblingOfType<LmQualifiedIdPartImplMixin>()
        return forIdentifier(prev, id)
    }

    private fun forIdentifier(id: LmQualifiedIdPartImplMixin?, from: LmQualifiedIdPart): Scope {
        id ?: return forElement(from.parent, from)
        val resolved = id.scope.resolve(id.name)
        return when (resolved) {
            is LmFile -> NamespaceProvider.forFile(resolved, true)
            is LmDirectory -> NamespaceProvider.forDirectory(resolved)
            is LmModule -> NamespaceProvider.forModule(resolved, true)
            else -> EmptyScope
        }
    }

    private fun forElement(element: PsiElement?, from: PsiElement, useCommand: Boolean = true): Scope {
        element ?: return LocalScope()
        val useCommand = useCommand && element !is LmCommand
        val parentScope = forElement(element.parent, element, useCommand)
        val scope = when (element) {
            is LmFile -> forFile(element, useCommand)
            is LmModule -> forModule(element, useCommand)
            is LmFunction -> forFunction(element)
            is LmLet -> forLet(element, from)
            else -> EmptyScope
        }
        return when {
            parentScope.items.isEmpty() -> scope
            scope.items.isEmpty() -> parentScope
            else -> OverridingScope(parentScope, scope)
        }
    }

    private fun forFile(element: LmFile, useCommand: Boolean): Scope {
        val dirNamespace = element.parent?.let { NamespaceProvider.forDirectory(LmDirectory(it)) }
        val fileNamespace = NamespaceProvider.forFile(element, useCommand)
        return when {
            dirNamespace == null || dirNamespace.items.isEmpty() -> fileNamespace
            fileNamespace.items.isEmpty() -> dirNamespace
            else -> OverridingScope(dirNamespace, fileNamespace)
        }
    }

    private fun forModule(element: LmModule, useCommand: Boolean): Scope =
            NamespaceProvider.forModule(element, useCommand)

    private fun forFunction(element: LmFunction): Scope {
        val scope = LocalScope()
        scope.put(element.parameter)
        return scope
    }

    private fun forLet(element: LmLet, from: PsiElement): Scope {
        val scope = LocalScope()
        val letType = (element.letKw ?: element.letrecKw ?: element.letparKw)?.elementType
        val bindings = when {
            letType === LmElementTypes.LETREC_KW || from === element.expression ->
                element.bindingList
            letType === LmElementTypes.LET_KW ->
                element.bindingList.takeWhile { it !== from }
            else ->
                listOf()
        }
        bindings.forEach { scope.put(it) }
        return scope
    }
}

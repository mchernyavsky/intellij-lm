package org.lm.resolve

import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import org.lm.psi.*
import kotlin.Boolean
import org.lm.psi.ext.LmQualifiedIdPartImplMixin
import org.nbkit.common.psi.ext.childrenOfType
import org.nbkit.common.psi.ext.prevSiblingOfType
import org.nbkit.common.resolve.EmptyScope
import org.nbkit.common.resolve.LocalScope
import org.nbkit.common.resolve.OverridingScope
import org.nbkit.common.resolve.Scope

object ScopeProvider {
    fun getScope(element: LmQualifiedIdPart): Scope {
        return forIdentifier(element.prevSiblingOfType<LmQualifiedIdPartImplMixin>(), element)
    }

    private fun forIdentifier(anchor: LmQualifiedIdPartImplMixin?, element: PsiElement): Scope {
        return if (anchor != null) {
            val resolved = anchor.scope.resolve(anchor.name)
            when (resolved) {
                is LmModule -> NamespaceProvider.forModule(resolved, true)
                is LmFileWrapper -> NamespaceProvider.forFile(resolved, true)
                is LmDirectoryWrapper -> NamespaceProvider.forDirectory(resolved)
                else -> EmptyScope
            }
        } else {
            forElement(element.parent, element)
        }
    }

    private fun forElement(
            element: PsiElement?,
            prev: PsiElement,
            useCommand: Boolean = true
    ): Scope {
        element ?: return LocalScope()
        val useCommand = useCommand && element !is LmCommand
        val scope = when (element) {
            is LmFunction -> forFunction(element, prev)
            is LmLetSeq -> forLetSeq(element, prev)
            is LmLetRec -> forLetRec(element, prev)
            is LmLetPar -> forLetPar(element, prev)
            is LmModule -> forModule(element, useCommand)
            is LmFile -> return forFile(LmFileWrapper(element), useCommand)
//            is PsiDirectory -> NamespaceProvider.forDirectory(LmDirectoryWrapper(element))
            else -> EmptyScope
        }
        val parentScope = forElement(element.parent, element, useCommand)
        return when {
            parentScope.items.isEmpty() -> scope
            scope.items.isEmpty() -> parentScope
            else -> OverridingScope(parentScope, scope)
        }
    }

    private fun forFunction(element: LmFunction, prev: PsiElement): Scope {
        val scope = LocalScope()
        element
                .childrenOfType<LmParameter>()
                .forEach { scope.put(it) }
        return scope
    }

    private fun forLetSeq(element: LmLetSeq, prev: PsiElement): Scope {
        val scope = LocalScope()
        when (prev) {
            is LmExpression -> {
                element
                        .childrenOfType<LmBinding>()
                        .forEach { scope.put(it) }
            }
            is LmBinding -> {
                element
                        .childrenOfType<LmBinding>()
                        .takeWhile { it !== prev }
                        .forEach { scope.put(it) }
            }
            else -> Unit
        }
        return scope
    }

    private fun forLetRec(element: LmLetRec, prev: PsiElement): Scope {
        val scope = LocalScope()
        element
                .childrenOfType<LmBinding>()
                .forEach { scope.put(it) }
        return scope
    }

    private fun forLetPar(element: LmLetPar, prev: PsiElement): Scope {
        val scope = LocalScope()
        when (prev) {
            is LmExpression -> {
                element
                        .childrenOfType<LmBinding>()
                        .forEach { scope.put(it) }
            }
            else -> Unit
        }
        return scope
    }

    private fun forModule(module: LmModule, useCommand: Boolean = true): Scope
            = NamespaceProvider.forModule(module, useCommand)

    private fun forFile(file: LmFileWrapper, useCommand: Boolean = true): Scope {
        val dirNamespace = file.parent?.let {
            NamespaceProvider.forDirectory(LmDirectoryWrapper(it))
        }
        val fileNamespace = NamespaceProvider.forFile(file, useCommand)
        return when {
            dirNamespace == null || dirNamespace.items.isEmpty() -> fileNamespace
            fileNamespace.items.isEmpty() -> dirNamespace
            else -> OverridingScope(dirNamespace, fileNamespace)
        }
    }
}

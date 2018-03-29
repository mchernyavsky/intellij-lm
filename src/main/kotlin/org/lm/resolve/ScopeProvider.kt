package org.lm.resolve

import org.lm.psi.*
import org.lm.psi.ext.LmElement
import org.lm.psi.ext.elementType

object ScopeProvider {

    fun forElement(
            element: LmElement,
            scope: LocalScope = LocalScope(),
            isLexical: Boolean = true
    ): Scope = when (element) {
        is LmFile -> forFile(element, scope, !isLexical)
        is LmModule -> forModule(element, scope, !isLexical)
        is LmFunction -> forFunction(element, scope)
        is LmLet -> forLet(element, scope)
        is LmBinding -> forBinding(element, scope)
        else -> scope
    }

    fun forFile(
            file: LmFile,
            scope: LocalScope = LocalScope(),
            withCommands: Boolean = true
    ): Scope {
        if (withCommands) {
            file.children
                    .filterIsInstance<LmStatement>()
                    .mapNotNull { it.command }
                    .map {
                        it.qualifiedId
                                .children
                                .filterIsInstance<LmQualifiedIdPart>()
                                .last()
                    }
                    .map { it.namespace }
                    .forEach { scope.putAll(it) }
        }
        NamespaceProvider.forFile(file, scope)
        return scope
    }

    fun forModule(
            module: LmModule,
            scope: LocalScope = LocalScope(),
            withCommands: Boolean = true
    ): Scope {
        if (withCommands) {
            module.statementList
                    .mapNotNull { it.command }
                    .map {
                        it.qualifiedId
                                .children
                                .filterIsInstance<LmQualifiedIdPart>()
                                .last()
                    }
                    .map { it.namespace }
                    .forEach { scope.putAll(it) }
        }
        NamespaceProvider.forModule(module, scope)
        return scope
    }

    fun forFunction(
            function: LmFunction,
            scope: LocalScope = LocalScope()
    ): Scope {
        scope.put(function.parameter)
        return scope
    }

    fun forLet(
            let: LmLet,
            scope: LocalScope = LocalScope()
    ): Scope {
        let.bindingList.forEach { scope.put(it) }
        return scope
    }

    fun forBinding(
            binding: LmBinding,
            scope: LocalScope = LocalScope()
    ): Scope {
        val parent = binding.parent as LmLet
        val letType = (parent.letKw ?: parent.letrecKw ?: parent.letparKw)?.elementType
        when (letType) {
            LmElementTypes.LET_KW -> {
                parent.bindingList
                        .takeWhile { it != binding }
                        .forEach { scope.put(it) }
            }
            LmElementTypes.LETREC_KW -> {
                parent.bindingList
                        .forEach { scope.put(it) }
            }
            else -> {}
        }
        return scope
    }
}

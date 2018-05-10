package org.lm.resolve

import org.lm.psi.*
import org.lm.psi.ext.LmElement
import org.lm.psi.ext.LmQualifiedIdPartImplMixin
import org.nbkit.common.psi.ext.childrenOfType
import org.nbkit.common.resolve.EmptyScope
import org.nbkit.common.resolve.LocalScope
import org.nbkit.common.resolve.Scope

object NamespaceProvider {

    fun forFile(
            file: LmFileWrapper,
            withCommands: Boolean,
            namespace: LocalScope = LocalScope()
    ): Scope {
        if (withCommands) {
            handleCommands(file, namespace)
        }
        file
                .childrenOfType<LmDefinition>()
                .forEach { namespace.put(it) }
        return namespace
    }

    fun forDirectory(directory: LmDirectoryWrapper, namespace: LocalScope = LocalScope()): Scope {
        directory.files
                .filterIsInstance<LmFile>()
                .map { LmFileWrapper(it) }
                .forEach { namespace.put(it) }
        directory.subdirectories
                .map { LmDirectoryWrapper(it) }
                .forEach { namespace.put(it) }
        return namespace
    }

    fun forModule(
            module: LmModule,
            withCommands: Boolean,
            namespace: LocalScope = LocalScope()
    ): Scope {
        if (withCommands) {
            handleCommands(module, namespace)
        }
        module
                .childrenOfType<LmDefinition>()
                .forEach { namespace.put(it) }
        return namespace
    }

    private fun handleCommands(element: LmElement, namespace: LocalScope) {
        element
                .childrenOfType<LmCommand>()
                .mapNotNull {
                    it
                            .childrenOfType<LmQualifiedId>()
                            .childrenOfType<LmQualifiedIdPartImplMixin>()
                            .lastOrNull()
                }
                .mapNotNull { it.scope.resolve(it.name) }
                .mapNotNull {
                    when (it) {
                        is LmFileWrapper -> forFile(it, false)
                        is LmDirectoryWrapper -> forDirectory(it)
                        is LmModule -> forModule(it, false)
                        else -> EmptyScope
                    }
                }
                .forEach { namespace.putAll(it) }
    }
}

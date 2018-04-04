package org.lm.resolve

import org.lm.psi.LmDirectory
import org.lm.psi.LmFile
import org.lm.psi.LmModule
import org.lm.psi.LmStatement
import org.lm.psi.ext.LmElement
import org.lm.psi.ext.LmQualifiedIdPartImplMixin

object NamespaceProvider {

    fun forFile(
            file: LmFile,
            withCommands: Boolean,
            namespace: LocalScope = LocalScope()
    ): Scope {
        if (withCommands) {
            handleCommands(file, namespace)
        }
        file.children
                .filterIsInstance<LmStatement>()
                .mapNotNull { it.definition }
                .forEach { namespace.put(it) }
        return namespace
    }

    fun forDirectory(
            dir: LmDirectory,
            namespace: LocalScope = LocalScope()
    ): Scope {
        dir.files
                .filterIsInstance<LmFile>()
                .forEach { namespace.put(it.nameWithoutSuffix, it) }
        dir.subdirectories
                .map { LmDirectory(it) }
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
        module.statementList
                .mapNotNull { it.definition }
                .forEach { namespace.put(it) }
        return namespace
    }

    private fun handleCommands(element: LmElement, namespace: LocalScope) {
        element.children
                .filterIsInstance<LmStatement>()
                .mapNotNull { it.command }
                .map {
                    it.qualifiedId
                            .children
                            .filterIsInstance<LmQualifiedIdPartImplMixin>()
                            .last()
                }
                .map { it.scope.resolve(it.name) }
                .mapNotNull {
                    when (it) {
                        is LmFile -> forFile(it, false)
                        is LmDirectory -> forDirectory(it)
                        is LmModule -> forModule(it, false)
                        else -> EmptyScope
                    }
                }
                .forEach { namespace.putAll(it) }
    }
}

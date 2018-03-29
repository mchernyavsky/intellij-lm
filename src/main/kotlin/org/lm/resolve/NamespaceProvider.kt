package org.lm.resolve

import org.lm.psi.LmDirectory
import org.lm.psi.LmFile
import org.lm.psi.LmModule
import org.lm.psi.LmStatement

object NamespaceProvider {

    fun forFile(
            file: LmFile,
            namespace: LocalScope = LocalScope(),
            withCommands: Boolean = true
    ): Scope {
        file.children
                .filterIsInstance<LmStatement>()
                .mapNotNull { it.definition }
                .forEach { namespace.put(it) }
        return namespace
    }

    fun forModule(
            module: LmModule,
            namespace: LocalScope = LocalScope(),
            withCommands: Boolean = true
    ): Scope {
        module.statementList
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
}

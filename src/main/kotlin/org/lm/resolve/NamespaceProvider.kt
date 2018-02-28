package org.lm.resolve

import org.lm.psi.LmModule
import org.lm.resolve.namespace.Namespace
import org.lm.resolve.namespace.SimpleNamespace

object NamespaceProvider {
    fun forModule(
            module: LmModule,
            namespace: SimpleNamespace = SimpleNamespace()
    ): Namespace {
        module.statementList
                .mapNotNull { it.definition }
                .forEach { namespace.put(it) }
        return namespace
    }
}

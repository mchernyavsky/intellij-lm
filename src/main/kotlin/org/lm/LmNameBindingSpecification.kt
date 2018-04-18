package org.lm

import com.squareup.kotlinpoet.asClassName
import org.lm.psi.*
import org.nbkit.NameBindingSpecification

fun main(args: Array<String>) {
    val typeNameProvider =
            listOf(
                    LmDefinitionId::class, LmQualifiedIdPart::class,
                    LmModule::class, LmVariable::class,
                    LmCommand::class, LmFunction::class,
                    LmLet::class, LmStatement::class
            )
            .map { it.simpleName!! to it.asClassName() }
            .toMap()

    val spec = NameBindingSpecification("Lm", "org.lm", typeNameProvider)
    spec.generate()
}

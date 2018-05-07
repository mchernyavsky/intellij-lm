package org.lm

import org.lm.psi.*
import org.nbkit.ScopeRuleSpec
import org.nbkit.gen.NameBindingSpec
import java.nio.file.Paths

fun main(args: Array<String>) {
    val scopeRules = listOf(
            // imports
            ScopeRuleSpec(LmCommand::class)
                    .build(),

            // named element
            ScopeRuleSpec(LmParameter::class)
                    .setIsNamedElement()
                    .build(),
            ScopeRuleSpec(LmBinding::class)
                    .setIsNamedElement()
                    .build(),
            // definitions
            ScopeRuleSpec(LmVariable::class)
                    .setIsStubbed()
                    .setIsNamedElement()
                    .setIsDefinition()
                    .build(),
            // classes
            ScopeRuleSpec(LmModule::class)
                    .setIsStubbed()
                    .setIsNamedElement()
                    .setIsDefinition()
                    .setIsClass()
                    .build(),

            // identifier
            ScopeRuleSpec(LmDefinitionId::class)
                    .setIsReferable()
                    .build(),
            ScopeRuleSpec(LmQualifiedId::class)
                    .build(),
            ScopeRuleSpec(LmQualifiedIdPart::class)
                    .setIsReference()
                    .build(),

            // other
            ScopeRuleSpec(LmStatement::class)
                    .build(),
            ScopeRuleSpec(LmExpression::class)
                    .build(),
            ScopeRuleSpec(LmLet::class)
                    .build()
    )

    NameBindingSpec(
            "Lm",
            "org.lm",
            Paths.get("src/gen_nb"),
            scopeRules
    ).generate()
}

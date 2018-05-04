package org.lm

import org.lm.psi.*
import org.nbkit.NameBindingSpec
import org.nbkit.ScopeRuleSpec

fun main(args: Array<String>) {
    NameBindingSpec("Lm", "org.lm")
            // imports
            .addScopeRule(ScopeRuleSpec(LmCommand::class)
                    .build())

            // named element
            .addScopeRule(ScopeRuleSpec(LmParameter::class)
                    .setIsNamedElement()
                    .build())
            .addScopeRule(ScopeRuleSpec(LmBinding::class)
                    .setIsNamedElement()
                    .build())
                // definitions
                .addScopeRule(ScopeRuleSpec(LmVariable::class)
                        .setIsStubbed()
                        .setIsNamedElement()
                        .setIsDefinition()
                        .build())
                    // classes
                    .addScopeRule(ScopeRuleSpec(LmModule::class)
                            .setIsStubbed()
                            .setIsNamedElement()
                            .setIsDefinition()
                            .setIsClass()
                            .build())

            // identifier
            .addScopeRule(ScopeRuleSpec(LmDefinitionId::class)
                    .setIsReferable()
                    .build())
            .addScopeRule(ScopeRuleSpec(LmQualifiedId::class)
                    .build())
            .addScopeRule(ScopeRuleSpec(LmQualifiedIdPart::class)
                    .setIsReference()
                    .build())

            // other
            .addScopeRule(ScopeRuleSpec(LmStatement::class)
                    .build())
            .addScopeRule(ScopeRuleSpec(LmExpression::class)
                    .build())
            .addScopeRule(ScopeRuleSpec(LmFunction::class)
                    .build())
            .addScopeRule(ScopeRuleSpec(LmLet::class)
                    .build())
    .generate()
}

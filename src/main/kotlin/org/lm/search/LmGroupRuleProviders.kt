package org.lm.search

import com.intellij.openapi.project.Project
import com.intellij.usages.PsiNamedElementUsageGroupBase
import com.intellij.usages.Usage
import com.intellij.usages.UsageGroup
import com.intellij.usages.UsageTarget
import com.intellij.usages.impl.FileStructureGroupRuleProvider
import com.intellij.usages.rules.PsiElementUsage
import com.intellij.usages.rules.SingleParentUsageGroupingRule
import com.intellij.usages.rules.UsageGroupingRule
import org.lm.psi.LmModule
import org.lm.psi.LmVariable
import org.lm.psi.ext.LmNamedElement
import org.lm.psi.parentOfType

class LmModuleGroupingRuleProvider : FileStructureGroupRuleProvider {
    override fun getUsageGroupingRule(project: Project): UsageGroupingRule? =
        createGroupingRule<LmModule>()
}

class LmVariableGroupingRuleProvider : FileStructureGroupRuleProvider {
    override fun getUsageGroupingRule(project: Project): UsageGroupingRule? =
        createGroupingRule<LmVariable>()
}

private inline fun <reified T : LmNamedElement> createGroupingRule(): UsageGroupingRule {
    return object : SingleParentUsageGroupingRule() {
        override fun getParentGroupFor(usage: Usage, targets: Array<out UsageTarget>): UsageGroup? {
            if (usage !is PsiElementUsage) return null
                val parent = usage.element.parentOfType<T>()
            return parent?.let { PsiNamedElementUsageGroupBase<LmNamedElement>(it) }
        }
    }
}

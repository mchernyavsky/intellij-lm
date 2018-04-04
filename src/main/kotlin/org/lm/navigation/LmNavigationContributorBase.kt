package org.lm.navigation

import com.intellij.navigation.GotoClassContributor
import com.intellij.navigation.NavigationItem
import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.StubIndex
import com.intellij.psi.stubs.StubIndexKey
import org.lm.psi.ext.LmNamedElement
import org.lm.psi.ext.fullName
import org.lm.psi.ext.module

abstract class VcNavigationContributorBase<T> protected constructor(
        private val indexKey: StubIndexKey<String, T>,
        private val clazz: Class<T>
) : GotoClassContributor where T : NavigationItem, T : LmNamedElement {

    override fun getNames(project: Project?, includeNonProjectItems: Boolean): Array<String> {
        project ?: return emptyArray()
        return StubIndex.getInstance().getAllKeys(indexKey, project).toTypedArray()
    }

    override fun getItemsByName(
            name: String?,
            pattern: String?,
            project: Project?,
            includeNonProjectItems: Boolean
    ): Array<NavigationItem> {
        if (project == null || name == null) return emptyArray()
        val scope = if (includeNonProjectItems) {
            GlobalSearchScope.allScope(project)
        } else {
            GlobalSearchScope.projectScope(project)
        }
        return StubIndex.getElements(indexKey, name, project, scope, clazz).toTypedArray()
    }

    override fun getQualifiedName(item: NavigationItem?): String? {
        if (item is LmNamedElement) {
            val name: String = item.name!!
            val module = item.module
            return if (module == null) name else "${module.fullName}.$name"
        }

        return null
    }

    override fun getQualifiedNameSeparator(): String = "."
}

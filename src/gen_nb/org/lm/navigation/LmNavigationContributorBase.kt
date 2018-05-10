package org.lm.navigation

import com.intellij.navigation.GotoClassContributor
import com.intellij.navigation.NavigationItem
import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.StubIndex
import com.intellij.psi.stubs.StubIndexKey
import java.lang.Class
import kotlin.Array
import kotlin.Boolean
import kotlin.String
import org.lm.psi.ext.LmNamedElement

abstract class LmNavigationContributorBase<T> protected constructor(private val indexKey: StubIndexKey<String, T>, private val clazz: Class<T>) : GotoClassContributor where T : NavigationItem, T : LmNamedElement {
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

    override fun getQualifiedName(item: NavigationItem?): String? = (item as? LmNamedElement)?.name

    override fun getQualifiedNameSeparator(): String = "."
}

package org.lm.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory
import org.lm.LmFileType

class LmPsiFactory(private val project: Project) {

    fun createDefinitionId(name: String): LmDefinitionId =
        createModule(name)?.definitionId ?: error("Failed to create definition id: `$name`")

    fun createModule(name: String): LmModule? {
        val code = "module $name {}"
        return createFromText(code)?.childOfType() ?: error("Failed to create module: `$name`")
    }

    private fun createFromText(code: String): LmFile? =
        PsiFileFactory.getInstance(project)
            .createFileFromText("DUMMY.lm", LmFileType, code) as? LmFile
}
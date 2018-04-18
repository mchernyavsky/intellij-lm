package org.lm.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory
import org.lm.LmFileType
import org.nbkit.common.psi.ext.childOfType

class LmPsiFactory(private val project: Project) {

    fun createDefinitionId(name: String): LmDefinitionId =
            createVariable(name)?.definitionId
                    ?: error("Failed to create definition id: `$name`")

    fun createQualifiedId(id: String): LmQualifiedId =
            createVariable("_", id)
                    ?.expression
                    ?.qualifiedId
                    ?: error("Failed to create qualified id: `$id`")

    fun createQualifiedIdPart(id: String): LmQualifiedIdPart =
            createQualifiedId(id).lastChild as? LmQualifiedIdPart
                    ?: error("Failed to create definition id: `$id`")

    fun createVariable(name: String, expression: String = "42"): LmVariable? {
        val code = "def $name = $expression"
        return createFromText(code)?.childOfType() ?: error("Failed to create variable: `$name`")
    }

    private fun createFromText(code: String): LmFile? =
            PsiFileFactory.getInstance(project)
                    .createFileFromText("DUMMY.lm", LmFileType, code) as? LmFile
}
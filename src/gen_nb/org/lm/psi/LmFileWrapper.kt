package org.lm.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import kotlin.String
import org.lm.LmFileType
import org.lm.psi.ext.LmNamedElement
import org.lm.resolve.LmReference

class LmFileWrapper(val file: LmFile) : LmNamedElement, PsiFile by file {
    override fun setName(name: String): PsiElement {
        val nameWithExtension = if (name.endsWith('.' + LmFileType.defaultExtension)) {
            name
        } else {
            name + '.' + LmFileType.defaultExtension
        }
        return file.setName(nameWithExtension)
    }

    override fun getName(): String = file.name.removeSuffix('.' + LmFileType.defaultExtension)

    override fun getReference(): LmReference? = null

    override fun getNameIdentifier(): PsiElement? = null
}

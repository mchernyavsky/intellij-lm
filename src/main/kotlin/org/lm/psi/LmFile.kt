package org.lm.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import org.lm.LmFileType
import org.lm.LmLanguage

open class LmFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, LmLanguage) {
    override fun getFileType(): FileType = LmFileType
    override fun toString(): String = "LM File"
}

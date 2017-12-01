package org.lm

import com.intellij.openapi.fileTypes.LanguageFileType

import javax.swing.Icon

object LmFileType : LanguageFileType(LmLanguage) {

    override fun getName(): String = "LM file"

    override fun getDescription(): String = "LM Files"

    override fun getDefaultExtension(): String = "lm"

    override fun getIcon(): Icon? = LmIcons.LM_FILE
}

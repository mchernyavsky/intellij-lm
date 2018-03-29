package org.lm.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import org.lm.LmFileType
import org.lm.LmLanguage
import org.lm.psi.ext.LmNamedElement
import org.lm.psi.stubs.LmFileStub
import org.lm.resolve.*

class LmFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, LmLanguage), LmNamedElement {
    override val namespace: Scope
        get() = NamespaceProvider.forFile(this)

    override val lexicalScope: Scope
        get() {
            val localScope = ScopeProvider.forElement(this)
            val parentScope = containingDirectory?.let {
                NamespaceProvider.forDirectory(LmDirectory(it))
            } ?: return localScope
            return when {
                localScope.items.isEmpty() -> parentScope
                parentScope.items.isEmpty() -> localScope
                else -> OverridingScope(parentScope, localScope)
            }
        }

    override val scope: Scope
        get() {
            val localScope = ScopeProvider.forElement(this, isLexical = false)
            val parentScope = containingDirectory?.let {
                NamespaceProvider.forDirectory(LmDirectory(it))
            } ?: return localScope
            return when {
                localScope.items.isEmpty() -> parentScope
                parentScope.items.isEmpty() -> localScope
                else -> OverridingScope(parentScope, localScope)
            }
        }

    val nameWithoutSuffix: String = name.removeSuffix('.' + LmFileType.defaultExtension)

    override fun setName(name: String): PsiElement {
        val nameWithExtension = if (name.endsWith('.' + LmFileType.defaultExtension)) {
            name
        } else {
            "$name.${LmFileType.defaultExtension}"
        }
        return super.setName(nameWithExtension)
    }

    override fun getStub(): LmFileStub? = super.getStub() as? LmFileStub

    override fun getReference(): LmReference? = null

    override fun getFileType(): FileType = LmFileType

    override fun toString(): String = "LM File"

    override fun getNameIdentifier(): PsiElement? = null
}

package org.lm.refactoring

import com.intellij.lang.refactoring.NamesValidator
import com.intellij.openapi.project.Project
import com.intellij.psi.tree.IElementType
import org.lm.lexer.LmLexerAdapter
import org.lm.psi.LM_KEYWORDS
import org.lm.psi.LmElementTypes.ID

class LmNamesValidator : NamesValidator {

    override fun isKeyword(name: String, project: Project?): Boolean =
        getLexerType(name) in LM_KEYWORDS

    override fun isIdentifier(name: String, project: Project?): Boolean =
        getLexerType(name) == ID && !containsComment(name)

    private fun containsComment(name: String): Boolean = name.contains("#")

    private fun getLexerType(text: String): IElementType? {
        val lexer = LmLexerAdapter()
        lexer.start(text)
        return if (lexer.tokenEnd == text.length) lexer.tokenType else null
    }
}

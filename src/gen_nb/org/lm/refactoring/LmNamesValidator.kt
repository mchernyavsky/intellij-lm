package org.lm.refactoring

import com.intellij.lang.refactoring.NamesValidator
import com.intellij.openapi.project.Project
import com.intellij.psi.tree.IElementType
import kotlin.Boolean
import kotlin.String
import org.lm.lexer.LmLexerAdapter
import org.lm.psi.LmTokenType

class LmNamesValidator : NamesValidator {
    override fun isKeyword(name: String, project: Project?): Boolean = getLexerType(name) in LmTokenType.keywords

    override fun isIdentifier(name: String, project: Project?): Boolean = getLexerType(name) in LmTokenType.identifiers && !containsComment(name)

    private fun containsComment(name: String): Boolean = name.contains("#")

    private fun getLexerType(text: String): IElementType? {
        val lexer = LmLexerAdapter()
        lexer.start(text)
        return if (lexer.tokenEnd == text.length) lexer.tokenType else null
    }
}

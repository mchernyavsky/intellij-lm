package org.lm.highlight

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import org.lm.lexer.LmLexerAdapter
import org.lm.psi.LmElementTypes.*
import org.lm.psi.LmTokenType

class LmSyntaxHighlighter : SyntaxHighlighterBase() {

    override fun getHighlightingLexer(): Lexer = LmLexerAdapter()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> =
            pack(map(tokenType)?.textAttributesKey)

    companion object {
        fun map(tokenType: IElementType?): LmHighlightingColors? = when (tokenType) {
            in LmTokenType.identifiers -> LmHighlightingColors.IDENTIFIER
            INT -> LmHighlightingColors.INT
            in LmTokenType.keywords -> LmHighlightingColors.KEYWORD
            in LmTokenType.operators -> LmHighlightingColors.OPERATORS
            DOT -> LmHighlightingColors.DOT
            LBRACE, RBRACE -> LmHighlightingColors.BRACES
            LPAREN, RPAREN -> LmHighlightingColors.PARENTHESIS
            in LmTokenType.comments -> LmHighlightingColors.LINE_COMMENT
            TokenType.BAD_CHARACTER -> LmHighlightingColors.BAD_CHARACTER
            else -> null
        }
    }
}

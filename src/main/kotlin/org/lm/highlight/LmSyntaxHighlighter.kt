package org.lm.highlight

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import org.lm.lexer.LmLexerAdapter
import org.lm.psi.LM_KEYWORDS
import org.lm.psi.LM_OPERATORS
import org.lm.psi.LmElementTypes.*

class LmSyntaxHighlighter : SyntaxHighlighterBase() {

    override fun getHighlightingLexer(): Lexer = LmLexerAdapter()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> =
            pack(map(tokenType)?.textAttributesKey)

    companion object {
        fun map(tokenType: IElementType?): LmHighlightingColors? = when (tokenType) {
            ID -> LmHighlightingColors.IDENTIFIER
            INT -> LmHighlightingColors.INT
            in LM_KEYWORDS -> LmHighlightingColors.KEYWORD
            in LM_OPERATORS -> LmHighlightingColors.OPERATORS
            DOT -> LmHighlightingColors.DOT
            LBRACE, RBRACE -> LmHighlightingColors.BRACES
            LPAREN, RPAREN -> LmHighlightingColors.PARENTHESIS
            LINE_COMMENT -> LmHighlightingColors.LINE_COMMENT
            TokenType.BAD_CHARACTER -> LmHighlightingColors.BAD_CHARACTER
            else -> null
        }
    }
}

package org.lm.psi

import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import org.lm.LmLanguage
import org.lm.psi.LmElementTypes.*

class LmTokenType(debugName: String) : IElementType(debugName, LmLanguage) {
    companion object {
        val keywords: TokenSet = TokenSet.create(
                MODULE_KW, IMPORT_KW, INCLUDE_KW,
                DEF_KW, IN_KW,
                FUN_KW, FIX_KW,
                LET_KW, LETREC_KW, LETPAR_KW,
                IFZ_KW, THEN_KW, ELSE_KW
        )
        val operators: TokenSet = TokenSet.create(ADD, SUB, MUL, DIV)
        val whitespaces: TokenSet = TokenSet.create(TokenType.WHITE_SPACE)
        val identifiers: TokenSet = TokenSet.create(ID)
        val comments: TokenSet = TokenSet.create(LINE_COMMENT)
        val literals: TokenSet = TokenSet.EMPTY
    }
}

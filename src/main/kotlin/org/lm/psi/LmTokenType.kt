package org.lm.psi

import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import org.lm.LmLanguage
import org.lm.psi.LmElementTypes.*

class LmTokenType(debugName: String) : IElementType(debugName, LmLanguage)

val LM_KEYWORDS: TokenSet = TokenSet.create(
        MODULE_KW, IMPORT_KW, INCLUDE_KW,
        DEF_KW, IN_KW,
        FUN_KW, FIX_KW,
        LET_KW, LETREC_KW, LETPAR_KW,
        IFZ_KW, THEN_KW, ELSE_KW
)

val LM_OPERATORS: TokenSet = TokenSet.create(ADD, SUB, MUL, DIV)

val LM_WHITE_SPACES: TokenSet = TokenSet.create(TokenType.WHITE_SPACE)

val LM_COMMENTS: TokenSet = TokenSet.create(LINE_COMMENT)

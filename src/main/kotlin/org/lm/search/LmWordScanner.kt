package org.lm.search

import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.psi.tree.TokenSet
import org.lm.lexer.LmLexerAdapter
import org.lm.psi.LM_COMMENTS
import org.lm.psi.LmElementTypes.ID

class LmWordScanner : DefaultWordsScanner(
        LmLexerAdapter(),
        TokenSet.create(ID),
        LM_COMMENTS,
        TokenSet.EMPTY
)

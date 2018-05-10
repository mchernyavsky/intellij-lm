package org.lm.search

import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import org.lm.lexer.LmLexerAdapter
import org.lm.psi.LmTokenType

class LmWordScanner : DefaultWordsScanner(LmLexerAdapter(), LmTokenType.identifiers, LmTokenType.comments, LmTokenType.literals)

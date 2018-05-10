package org.lm.refactoring

import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import org.lm.lexer.LmLexerAdapter
import org.lm.psi.LmTokenType

class LmRefactoringSupportProvider : DefaultWordsScanner(LmLexerAdapter(), LmTokenType.identifiers, LmTokenType.comments, LmTokenType.literals)

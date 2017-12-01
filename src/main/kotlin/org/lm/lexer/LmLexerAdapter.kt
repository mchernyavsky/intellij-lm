package org.lm.lexer

import com.intellij.lexer.FlexAdapter
import org.lm.lang.lexer._LmLexer
import java.io.Reader

class LmLexerAdapter : FlexAdapter(_LmLexer(null as Reader?))

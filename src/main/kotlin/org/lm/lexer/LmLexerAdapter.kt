package org.lm.lexer

import com.intellij.lexer.FlexAdapter
import java.io.Reader

class LmLexerAdapter : FlexAdapter(org.lm.lexer._LmLexer(null as Reader?))

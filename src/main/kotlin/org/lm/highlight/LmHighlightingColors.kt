package org.lm.highlight

import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors as Default

enum class LmHighlightingColors(humanName: String, default: TextAttributesKey) {
    IDENTIFIER("Identifier", Default.IDENTIFIER),
    INT("Int", Default.NUMBER),
    KEYWORD("Keyword", Default.KEYWORD),
    DECLARATION("Declaration", Default.FUNCTION_DECLARATION),
    OPERATORS("Operator sign", Default.COMMA),
    DOT("Dot", Default.COMMA),
    BRACES("Braces", Default.BRACES),
    PARENTHESIS("Parenthesis", Default.PARENTHESES),
    LINE_COMMENT("Line comment", Default.LINE_COMMENT),
    BAD_CHARACTER("Bad character", HighlighterColors.BAD_CHARACTER);

    val textAttributesKey = TextAttributesKey.createTextAttributesKey("org.lm.$name", default)
}

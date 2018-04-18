package org.lm.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static org.lm.psi.LmElementTypes.*;

%%

%{
    public _LmLexer() {
        this((java.io.Reader) null);
    }
%}

%public
%class _LmLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL                 = \R
WHITE_SPACE         = \s+

LINE_COMMENT        = #(.*|{EOL})

ID                  = [a-zA-Z_][a-zA-Z0-9_]*
INT                 = 0|[1-9][0-9]*

%%
<YYINITIAL> {
    {WHITE_SPACE}  { return WHITE_SPACE; }

    "("            { return LPAREN; }
    ")"            { return RPAREN; }
    "{"            { return LBRACE; }
    "}"            { return RBRACE; }

    "="            { return ASSIGN; }
    "."            { return DOT; }

    "+"            { return ADD; }
    "-"            { return SUB; }
    "*"            { return MUL; }
    "/"            { return DIV; }

    "module"       { return MODULE_KW; }
    "import"       { return IMPORT_KW; }
    "include"      { return INCLUDE_KW; }

    "def"          { return DEF_KW; }

    "fun"          { return FUN_KW; }
    "fix"          { return FIX_KW; }

    "let"          { return LET_KW; }
    "letrec"       { return LETREC_KW; }
    "letpar"       { return LETPAR_KW; }
    "in"           { return IN_KW; }

    "ifz"          { return IFZ_KW; }
    "then"         { return THEN_KW; }
    "else"         { return ELSE_KW; }

    {ID}           { return ID; }
    {INT}          { return INT; }

    {LINE_COMMENT} { return LINE_COMMENT; }
}

[^] { return BAD_CHARACTER; }

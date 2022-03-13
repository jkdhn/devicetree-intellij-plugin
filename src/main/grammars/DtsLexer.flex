package me.jkdhn.devicetree.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import me.jkdhn.devicetree.parser.DtsParserDefinition;import me.jkdhn.devicetree.psi.DtsTypes;
import com.intellij.psi.TokenType;

%%

%class _DtsLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType

%{
  private int zzOldState;
  private int zzRefState;
  private int zzPreState;
%}

HEADER_V1 = "/dts-v1/"

WHITE_SPACE_EOL=\n | \r\n
WHITE_SPACE_INLINE=\  | \t
WHITE_SPACE_ANY={WHITE_SPACE_EOL} | {WHITE_SPACE_INLINE}
WHITE_SPACE={WHITE_SPACE_ANY}+

LITERAL_INT = {LITERAL_INT_DEF} | {LITERAL_INT_BIN} | {LITERAL_INT_HEX}
LITERAL_INT_DEF = [0-9]+ // decimal or octal
LITERAL_INT_BIN = "0b" [01]+
LITERAL_INT_HEX = "0x" [0-9a-fA-F]+

LITERAL_STRING = \" {LITERAL_STRING_CHAR}* \"
LITERAL_STRING_CHAR = [^\\\"] | \\[^]

LABEL_NAME = [a-zA-Z_] [0-9a-zA-Z_]{0,30}

NODE_NAME = \/ | ([a-zA-Z] [0-9a-zA-Z,._+-]{0,30})
UNIT_ADDRESS = [0-9a-zA-Z,._+-]+

PROPERTY_NAME = [0-9a-zA-Z,._+?#-]{1,31}

ANY = [0-9a-zA-Z,._+?#\s@-]*

%xstate PREPROCESSOR_DIRECTIVE
%xstate PREPROCESSOR_VALUE
%state BLOCK_COMMENT
%state REFERENCE
%state NODE_NAME
%state UNIT_ADDRESS
%state VALUE
%state CELL

%%

    ^ "#" .* \R                     { zzPreState = yystate(); yybegin(PREPROCESSOR_DIRECTIVE); yypushback(yylength()); }
    "//" .* \R                      { return DtsParserDefinition.LINE_COMMENT; }
    "/*"                            { zzOldState = yystate(); yybegin(BLOCK_COMMENT); }

<PREPROCESSOR_DIRECTIVE> {
    "#include"                      { yybegin(PREPROCESSOR_VALUE); return DtsParserDefinition.PRE_INCLUDE; }
    "# " .*                         { return DtsParserDefinition.LINE_COMMENT; }
    \n                              { yybegin(zzPreState); return TokenType.WHITE_SPACE; }
    [^]                             { return TokenType.BAD_CHARACTER; }
}

<PREPROCESSOR_VALUE> {
    \< [^\n>]+  \>                  { return DtsParserDefinition.PRE_HEADER; }
    \" [^\n\"]+ \"                  { return DtsParserDefinition.PRE_HEADER; }
    {WHITE_SPACE_INLINE}+           { return TokenType.WHITE_SPACE; }
    \n                              { yybegin(zzPreState); return TokenType.WHITE_SPACE; }
    [^]                             { return TokenType.BAD_CHARACTER; }
}

<BLOCK_COMMENT> {
    "*/"                            { yybegin(zzOldState); return DtsParserDefinition.BLOCK_COMMENT; }
    [^]                             { }
}

<REFERENCE> {
    {LABEL_NAME}                    { return DtsTypes.LABEL_NAME; }
    [^]                             { yybegin(zzRefState); yypushback(1); }
}

<YYINITIAL> {
    {HEADER_V1}                     { return DtsTypes.HEADER_V1; }
    "{"                             { return DtsTypes.BRACE_LEFT; }
    "}"                             { return DtsTypes.BRACE_RIGHT; }
    ";"                             { return DtsTypes.SEMICOLON; }
    "="                             { yybegin(VALUE); return DtsTypes.EQ; }
    "&"                             { zzRefState = yystate(); yybegin(REFERENCE); return DtsTypes.AMPERSAND; }
    ":"                             { return DtsTypes.COLON; }
    {NODE_NAME} / {ANY} "{"         { yybegin(NODE_NAME); return DtsTypes.NODE_NAME; }
    {PROPERTY_NAME} / {ANY} [=;]    { return DtsTypes.PROPERTY_NAME; }
    {LABEL_NAME} / ":"              { return DtsTypes.LABEL_NAME; }
    {WHITE_SPACE}                   { return TokenType.WHITE_SPACE; }
    [^]                             { return TokenType.BAD_CHARACTER; }
}

<NODE_NAME> {
    "@"                             { yybegin(UNIT_ADDRESS); return DtsTypes.AT; }
    [^]                             { yybegin(YYINITIAL); yypushback(1); }
}

<UNIT_ADDRESS> {
    {UNIT_ADDRESS}                  { yybegin(YYINITIAL); return DtsTypes.UNIT_ADDRESS; }
    [^]                             { yybegin(YYINITIAL); yypushback(1); }
}

<VALUE> {
    {LITERAL_STRING}                { return DtsTypes.LITERAL_STRING; }
    ","                             { return DtsTypes.COMMA; }
    "<"                             { yybegin(CELL); return DtsTypes.CELL_BEGIN; }
    ";"                             { yybegin(YYINITIAL); return DtsTypes.SEMICOLON; }
    "&"                             { zzRefState = yystate(); yybegin(REFERENCE); return DtsTypes.AMPERSAND; }
    ":"                             { return DtsTypes.COLON; }
    {LABEL_NAME} / ":"              { return DtsTypes.LABEL_NAME; }
    {WHITE_SPACE}                   { return TokenType.WHITE_SPACE; }
    [^]                             { yybegin(YYINITIAL); yypushback(1); }
}

<CELL> {
    "("                             { return DtsTypes.PAR_LEFT; }
    ")"                             { return DtsTypes.PAR_RIGHT; }
    "+"                             { return DtsTypes.CELL_ADD; }
    "-"                             { return DtsTypes.CELL_SUB; }
    "*"                             { return DtsTypes.CELL_MUL; }
    "/"                             { return DtsTypes.CELL_DIV; }
    {LITERAL_INT}                   { return DtsTypes.CELL_INT; }
    "&"                             { return DtsTypes.AMPERSAND; }
    ":"                             { return DtsTypes.COLON; }
    {LABEL_NAME}                    { return DtsTypes.LABEL_NAME; }
    ">"                             { yybegin(VALUE); return DtsTypes.CELL_END; }
    ";"                             { yybegin(YYINITIAL); return DtsTypes.SEMICOLON; }
    {WHITE_SPACE}                   { return TokenType.WHITE_SPACE; }
    [^]                             { yybegin(YYINITIAL); yypushback(1); }
}

    [^]                             { return TokenType.BAD_CHARACTER; }

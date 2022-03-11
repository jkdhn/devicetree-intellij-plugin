package me.jkdhn.devicetree.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import me.jkdhn.devicetree.psi.DtsTypes;
import com.intellij.psi.TokenType;

%%

%public
%class DtsLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType

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
LABEL = {LABEL_NAME} :
REFERENCE = & {LABEL_NAME}

NODE_NAME = \/ | ([a-zA-Z] [0-9a-zA-Z,._+-]{0,30})
UNIT_ADDRESS = [0-9a-zA-Z,._+-]+

PROPERTY_NAME = [0-9a-zA-Z,._+?#-]{1,31}

DIRECTIVE_INCLUDE = "#include"

ANY = [0-9a-zA-Z,._+?#\s@-]*

%state V1
%state V1_DIRECTIVE
%state V1_BLOCK_COMMENT
%state V1_NODE_NAME
%state V1_UNIT_ADDRESS
%state V1_VALUE
%state V1_CELL

%%

<YYINITIAL> {
    {HEADER_V1}                     { yybegin(V1); return DtsTypes.HEADER_V1; }
}

<V1> {
    "{"                             { return DtsTypes.BRACE_LEFT; }
    "}"                             { return DtsTypes.BRACE_RIGHT; }
    ";"                             { return DtsTypes.SEMICOLON; }
    "="                             { yybegin(V1_VALUE); return DtsTypes.EQ; }
    "//" .*                         { return DtsTypes.LINE_COMMENT; }
    "/*"                            { yybegin(V1_BLOCK_COMMENT); }
    {NODE_NAME} / {ANY} "{"         { yybegin(V1_NODE_NAME); return DtsTypes.NODE_NAME; }
    {PROPERTY_NAME} / {ANY} [=;]    { return DtsTypes.PROPERTY_NAME; }
    {LABEL}                         { return DtsTypes.LABEL; }
    {REFERENCE}                     { return DtsTypes.REFERENCE; }
    {WHITE_SPACE}                   { return TokenType.WHITE_SPACE; }
    ^ {DIRECTIVE_INCLUDE}           { yybegin(V1_DIRECTIVE); return DtsTypes.INCLUDE_START; }
    [^]                             { return TokenType.BAD_CHARACTER; }
}

<V1_DIRECTIVE> {
    "<"                             { return DtsTypes.DIRECTIVE_START; }
    [a-zA-Z0-9/._-]+                 { return DtsTypes.DIRECTIVE_VALUE; }
    ">"                             { return DtsTypes.DIRECTIVE_END; }
    {WHITE_SPACE_INLINE}            { return TokenType.WHITE_SPACE; }
    \R                              { yybegin(V1); }
    [^]                             { yybegin(V1); yypushback(1); }
}

<V1_BLOCK_COMMENT> {
    "*/"                            { yybegin(V1); return DtsTypes.BLOCK_COMMENT; }
    [^]                             { }
}

<V1_NODE_NAME> {
    "@"                             { yybegin(V1_UNIT_ADDRESS); return DtsTypes.AT; }
    [^]                             { yybegin(V1); yypushback(1); }
}

<V1_UNIT_ADDRESS> {
    {UNIT_ADDRESS}                  { yybegin(V1); return DtsTypes.UNIT_ADDRESS; }
    [^]                             { yybegin(V1); yypushback(1); }
}

<V1_VALUE> {
    {LITERAL_STRING}                { return DtsTypes.LITERAL_STRING; }
    {REFERENCE}                     { return DtsTypes.REFERENCE; }
    ","                             { return DtsTypes.COMMA; }
    "<"                             { yybegin(V1_CELL); return DtsTypes.CELL_BEGIN; }
    ";"                             { yybegin(V1); return DtsTypes.SEMICOLON; }
    {LABEL}                         { return DtsTypes.LABEL; }
    {WHITE_SPACE}                   { return TokenType.WHITE_SPACE; }
    [^]                             { yybegin(V1); yypushback(1); }
}

<V1_CELL> {
    {LITERAL_INT}                   { return DtsTypes.LITERAL_INT; }
    {REFERENCE}                     { return DtsTypes.REFERENCE; }
    ">"                             { yybegin(V1_VALUE); return DtsTypes.CELL_END; }
    ";"                             { yybegin(V1); return DtsTypes.SEMICOLON; }
    {LABEL}                         { return DtsTypes.LABEL; }
    {WHITE_SPACE}                   { return TokenType.WHITE_SPACE; }
    [^]                             { yybegin(V1); yypushback(1); }
}

package me.jkdhn.devicetree.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import me.jkdhn.devicetree.parser.DtsParserDefinition;
import me.jkdhn.devicetree.preprocessor.psi.PreTokenTypes;
import me.jkdhn.devicetree.psi.DtsTypes;

%%

%class _PreLexer
%implements FlexLexer
%function advance
%type IElementType

WS=\  | \t | (\\ \n)

INTEGER = ([0-9]+) | ("0b" [01]+) | ("0x" [0-9a-fA-F]+)

//STRING = \" ( [^\\\"\n] | (\\ [^]) )* \"
STRING = \" [^\"\n]* \"

IDENTIFIER_NONDIGIT = [a-zA-Z_]
IDENTIFIER_CHAR = [0-9] | {IDENTIFIER_NONDIGIT}
IDENTIFIER = {IDENTIFIER_NONDIGIT} {IDENTIFIER_CHAR}*

%state PRE_DEFINE
%state PRE_DEFINE_IDENTIFIER
%state PRE_DEFINE_PARAMETERS
%state PRE_DEFINE_REPLACEMENT

%state PRE_INCLUDE
%state PRE_INCLUDE_H
%state PRE_INCLUDE_Q

%%

<YYINITIAL> {
    "#"                             { return PreTokenTypes.HASH; }
    "define"                        { yybegin(PRE_DEFINE); return PreTokenTypes.DIRECTIVE; }
    "include"                       { yybegin(PRE_INCLUDE); return PreTokenTypes.DIRECTIVE; }
    {WS}+                           { return TokenType.WHITE_SPACE; }
}

<PRE_DEFINE> {
    {IDENTIFIER}                    { yybegin(PRE_DEFINE_IDENTIFIER); return PreTokenTypes.DEFINE_IDENTIFIER; }
    {WS}+                           { return TokenType.WHITE_SPACE; }
}

<PRE_DEFINE_IDENTIFIER> {
    "("                             { yybegin(PRE_DEFINE_PARAMETERS); return PreTokenTypes.LPAR; }
    {WS}+                           { return TokenType.WHITE_SPACE; }
    [^]                             { yybegin(PRE_DEFINE_REPLACEMENT); yypushback(yylength()); }
}

<PRE_DEFINE_PARAMETERS> {
    {IDENTIFIER} | "..."            { return PreTokenTypes.DEFINE_PARAMETER; }
    ","                             { return PreTokenTypes.COMMA; }
    {WS}+                           { return TokenType.WHITE_SPACE; }
    ")"                             { yybegin(PRE_DEFINE_REPLACEMENT); return PreTokenTypes.RPAR; }
}

<PRE_DEFINE_REPLACEMENT> {
    {WS}+ / [^]*                    { return TokenType.WHITE_SPACE; }
    [^]+                            { return PreTokenTypes.DEFINE_REPLACEMENT; }
}

<PRE_INCLUDE> {
    \<                              { yybegin(PRE_INCLUDE_H); return PreTokenTypes.LT; }
    \"                              { yybegin(PRE_INCLUDE_Q); return PreTokenTypes.DQUOT; }
    {WS}+                           { return TokenType.WHITE_SPACE; }
}

<PRE_INCLUDE_H> {
    [^\n\>]+                        { return PreTokenTypes.INCLUDE_HEADER; }
    \>                              { return PreTokenTypes.GT; }
}

<PRE_INCLUDE_Q> {
    [^\n\"]+                        { return PreTokenTypes.INCLUDE_HEADER; }
    \"                              { return PreTokenTypes.DQUOT; }
}

    [^]                             { return TokenType.BAD_CHARACTER; }

package me.jkdhn.devicetree.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import me.jkdhn.devicetree.parser.DtsParserDefinition;
import me.jkdhn.devicetree.preprocessor.psi.PreTokenTypes;
import me.jkdhn.devicetree.psi.DtsTypes;

%%

%class _DtsLexer
%implements FlexLexer
%function advance
%type IElementType

HEADER_V1 = "/dts-v1/"

EOL=\n | \r\n
WS=\  | \t | (\\ \n)
WHITE_SPACE={WS}+ | {EOL}

INTEGER = ([0-9]+) | ("0b" [01]+) | ("0x" [0-9a-fA-F]+)

//STRING = \" ( [^\\\"\n] | (\\ [^]) )* \"
STRING = \" [^\"\n]* \"

IDENTIFIER_NONDIGIT = [a-zA-Z_]
IDENTIFIER_CHAR = [0-9] | {IDENTIFIER_NONDIGIT}
IDENTIFIER = {IDENTIFIER_NONDIGIT} {IDENTIFIER_CHAR}*

PRE_DIRECTIVE = {WS}* "#" {WS}*
//                (if|ifdef|ifndef|elif|else|endif|include|define|undef|line|error|pragma)
                (if|ifdef|ifndef|elif|else|endif|include|define)
                ([^\\\n] | (\\ [^]))* \n

NODE_NAME = [a-zA-Z] [0-9a-zA-Z,._+-]*
UNIT_ADDRESS = [0-9a-zA-Z,._+-]+
PROPERTY_NAME = [0-9a-zA-Z,._+?#-]+
LABEL_NAME = [a-zA-Z_] [0-9a-zA-Z_]*

%state AFTER_NODE_NAME
%state AFTER_NODE_NAME_AT
%state AFTER_AMPERSAND
%state AFTER_AMPERSAND_IN_VALUE
%state AFTER_AMPERSAND_IN_CELL
%state VALUE
%state CELL

%%

    ^ {PRE_DIRECTIVE}               { return DtsParserDefinition.PREPROCESSOR_DIRECTIVE; }
    "//" .* \R                      { return DtsParserDefinition.LINE_COMMENT; }
    "/*" ~"*/"                      { return DtsParserDefinition.BLOCK_COMMENT; }

    {HEADER_V1}                     { return DtsTypes.HEADER_V1; }
    "{"                             { return DtsTypes.BRACE_LEFT; }
    "}"                             { return DtsTypes.BRACE_RIGHT; }
    "("                             { return DtsTypes.PAR_LEFT; }
    ")"                             { return DtsTypes.PAR_RIGHT; }
    ";"                             { return DtsTypes.SEMICOLON; }
    ":"                             { return DtsTypes.COLON; }
    "+"                             { return DtsTypes.ADD; }
    "-"                             { return DtsTypes.SUB; }
    "*"                             { return DtsTypes.MUL; }
    "/"                             { return DtsTypes.SLASH; }
    {LABEL_NAME} / ":" .*           { return DtsTypes.LABEL_NAME; }

<YYINITIAL> {
    {NODE_NAME} / .* "{"            { yybegin(AFTER_NODE_NAME); return DtsTypes.NODE_NAME; }
    {PROPERTY_NAME}                 { return DtsTypes.PROPERTY_NAME; }
    "&"                             { yybegin(AFTER_AMPERSAND); return DtsTypes.AMPERSAND; }
    "="                             { yybegin(VALUE); return DtsTypes.EQ; }
    {WHITE_SPACE}                   { return TokenType.WHITE_SPACE; }
}

<AFTER_NODE_NAME> {
    "@"                             { yybegin(AFTER_NODE_NAME_AT); return DtsTypes.AT; }
    [^]                             { yybegin(YYINITIAL); yypushback(yylength()); }
}

<AFTER_NODE_NAME_AT> {
    {UNIT_ADDRESS}                  { yybegin(YYINITIAL); return DtsTypes.UNIT_ADDRESS; }
}

<AFTER_AMPERSAND> {
    {LABEL_NAME}                    { yybegin(YYINITIAL); return DtsTypes.LABEL_NAME; }
}

<AFTER_AMPERSAND_IN_CELL> {
    {LABEL_NAME}                    { yybegin(CELL); return DtsTypes.LABEL_NAME; }
}

<AFTER_AMPERSAND_IN_VALUE> {
    {LABEL_NAME}                    { yybegin(VALUE); return DtsTypes.LABEL_NAME; }
}

<VALUE> {
    {STRING}                        { return DtsTypes.STRING; }
    ","                             { return DtsTypes.COMMA; }
    "&"                             { yybegin(AFTER_AMPERSAND_IN_VALUE); return DtsTypes.AMPERSAND; }
    "<"                             { yybegin(CELL); return DtsTypes.LT; }
    {WHITE_SPACE}                   { return TokenType.WHITE_SPACE; }
    [^]                             { yybegin(YYINITIAL); yypushback(yylength()); }
}

<CELL> {
    {INTEGER}                       { return DtsTypes.INTEGER; }
    "&"                             { yybegin(AFTER_AMPERSAND_IN_CELL); return DtsTypes.AMPERSAND; }
    ">"                             { yybegin(VALUE); return DtsTypes.GT; }
    {WHITE_SPACE}                   { return TokenType.WHITE_SPACE; }
    {IDENTIFIER}                    { return PreTokenTypes.IDENTIFIER; }
}

    [^]                             { yybegin(YYINITIAL); return TokenType.BAD_CHARACTER; }

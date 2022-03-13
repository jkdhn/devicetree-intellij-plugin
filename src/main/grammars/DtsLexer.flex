package me.jkdhn.devicetree.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import me.jkdhn.devicetree.parser.DtsParserDefinition;
import me.jkdhn.devicetree.psi.DtsTypes;
import me.jkdhn.devicetree.psi.DtsPreTypes;
import java.util.Stack;

%%

%class _DtsLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType

HEADER_V1 = "/dts-v1/"

EOL=\n | \r\n
WS=\  | \t
WHITE_SPACE_ANY={EOL} | {WS}
WHITE_SPACE={WHITE_SPACE_ANY}+

INTEGER = ([0-9]+) | ("0b" [01]+) | ("0x" [0-9a-fA-F]+)

STRING = \" ( [^\\\"] | (\\ [^]) )* \"

IDENTIFIER = [0-9a-zA-Z#?@,._+-]+

PRE = {WS}* "#" {WS}*
PRE_END = ([^a-z_] .* \R) | \R

PRE_IF      = {PRE} "if"
PRE_IFDEF   = {PRE} "ifdef"
PRE_IFNDEF  = {PRE} "ifndef"
PRE_ELIF    = {PRE} "elif"
PRE_ELSE    = {PRE} "else"
PRE_ENDIF   = {PRE} "endif"
PRE_INCLUDE = {PRE} "include"
PRE_DEFINE  = {PRE} "define"
PRE_UNDEF   = {PRE} "undef"
PRE_LINE    = {PRE} "line"
PRE_ERROR   = {PRE} "error"
PRE_PRAGMA  = {PRE} "pragma"

%xstate PREPROCESSOR_DIRECTIVE
%xstate BLOCK_COMMENT

%%

<PREPROCESSOR_DIRECTIVE> {
    ([^\n] | (\\ \n))+              { return DtsPreTypes.CONTENT; }
    \n                              { yybegin(YYINITIAL); return DtsPreTypes.END; }
}

<BLOCK_COMMENT> {
    "*/"                            { yybegin(YYINITIAL); return DtsParserDefinition.BLOCK_COMMENT; }
    [^]                             { }
}

<YYINITIAL> {
    "//" .* \R                      { return DtsParserDefinition.LINE_COMMENT; }
    "/*"                            { yybegin(BLOCK_COMMENT); }
    ^ {PRE_IF}      / {PRE_END}     { yybegin(PREPROCESSOR_DIRECTIVE); return DtsPreTypes.IF; }
    ^ {PRE_IFDEF}   / {PRE_END}     { yybegin(PREPROCESSOR_DIRECTIVE); return DtsPreTypes.IFDEF; }
    ^ {PRE_IFNDEF}  / {PRE_END}     { yybegin(PREPROCESSOR_DIRECTIVE); return DtsPreTypes.IFNDEF; }
    ^ {PRE_ELIF}    / {PRE_END}     { yybegin(PREPROCESSOR_DIRECTIVE); return DtsPreTypes.ELIF; }
    ^ {PRE_ELSE}    / {PRE_END}     { yybegin(PREPROCESSOR_DIRECTIVE); return DtsPreTypes.ELSE; }
    ^ {PRE_ENDIF}   / {PRE_END}     { yybegin(PREPROCESSOR_DIRECTIVE); return DtsPreTypes.ENDIF; }
    ^ {PRE_INCLUDE} / {PRE_END}     { yybegin(PREPROCESSOR_DIRECTIVE); return DtsPreTypes.INCLUDE; }
    ^ {PRE_DEFINE}  / {PRE_END}     { yybegin(PREPROCESSOR_DIRECTIVE); return DtsPreTypes.DEFINE; }
    ^ {PRE_UNDEF}   / {PRE_END}     { yybegin(PREPROCESSOR_DIRECTIVE); return DtsPreTypes.UNDEF; }
    ^ {PRE_LINE}    / {PRE_END}     { yybegin(PREPROCESSOR_DIRECTIVE); return DtsPreTypes.LINE; }
    ^ {PRE_ERROR}   / {PRE_END}     { yybegin(PREPROCESSOR_DIRECTIVE); return DtsPreTypes.ERROR; }
    ^ {PRE_PRAGMA}  / {PRE_END}     { yybegin(PREPROCESSOR_DIRECTIVE); return DtsPreTypes.PRAGMA; }
    {HEADER_V1}                     { return DtsTypes.HEADER_V1; }
    "{"                             { return DtsTypes.BRACE_LEFT; }
    "}"                             { return DtsTypes.BRACE_RIGHT; }
    "("                             { return DtsTypes.PAR_LEFT; }
    ")"                             { return DtsTypes.PAR_RIGHT; }
    "<"                             { return DtsTypes.LT; }
    ">"                             { return DtsTypes.GT; }
    ";"                             { return DtsTypes.SEMICOLON; }
    "="                             { return DtsTypes.EQ; }
    "&"                             { return DtsTypes.AMPERSAND; }
    ":"                             { return DtsTypes.COLON; }
    "+"                             { return DtsTypes.ADD; }
    "-"                             { return DtsTypes.SUB; }
    "*"                             { return DtsTypes.MUL; }
    "/"                             { return DtsTypes.SLASH; }
    ","                             { return DtsTypes.COMMA; }
    {STRING}                        { return DtsTypes.STRING; }
    {INTEGER}                       { return DtsTypes.INTEGER; }
    {IDENTIFIER}                    { return DtsTypes.IDENTIFIER; }
    {WHITE_SPACE}                   { return TokenType.WHITE_SPACE; }
    [^]                             { return TokenType.BAD_CHARACTER; }
}

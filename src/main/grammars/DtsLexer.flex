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

%{
  private final Stack<Integer> zzStack = new Stack<>();

  private void yycall(int state) {
      zzStack.push(yystate());
      yybegin(state);
  }

  private void yyreturn() {
      yybegin(zzStack.pop());
  }
%}

HEADER_V1 = "/dts-v1/"

EOL=\n | \r\n
WS=\  | \t
WHITE_SPACE_ANY={EOL} | {WS}
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
%state REFERENCE
%state NODE_NAME
%state UNIT_ADDRESS
%state VALUE
%state CELL

%%

    "//" .* \R                      { return DtsParserDefinition.LINE_COMMENT; }
    "/*"                            { yycall(BLOCK_COMMENT); }

    ^ {PRE_IF}      / {PRE_END}     { yycall(PREPROCESSOR_DIRECTIVE); return DtsPreTypes.IF; }
    ^ {PRE_IFDEF}   / {PRE_END}     { yycall(PREPROCESSOR_DIRECTIVE); return DtsPreTypes.IFDEF; }
    ^ {PRE_IFNDEF}  / {PRE_END}     { yycall(PREPROCESSOR_DIRECTIVE); return DtsPreTypes.IFNDEF; }
    ^ {PRE_ELIF}    / {PRE_END}     { yycall(PREPROCESSOR_DIRECTIVE); return DtsPreTypes.ELIF; }
    ^ {PRE_ELSE}    / {PRE_END}     { yycall(PREPROCESSOR_DIRECTIVE); return DtsPreTypes.ELSE; }
    ^ {PRE_ENDIF}   / {PRE_END}     { yycall(PREPROCESSOR_DIRECTIVE); return DtsPreTypes.ENDIF; }
    ^ {PRE_INCLUDE} / {PRE_END}     { yycall(PREPROCESSOR_DIRECTIVE); return DtsPreTypes.INCLUDE; }
    ^ {PRE_DEFINE}  / {PRE_END}     { yycall(PREPROCESSOR_DIRECTIVE); return DtsPreTypes.DEFINE; }
    ^ {PRE_UNDEF}   / {PRE_END}     { yycall(PREPROCESSOR_DIRECTIVE); return DtsPreTypes.UNDEF; }
    ^ {PRE_LINE}    / {PRE_END}     { yycall(PREPROCESSOR_DIRECTIVE); return DtsPreTypes.LINE; }
    ^ {PRE_ERROR}   / {PRE_END}     { yycall(PREPROCESSOR_DIRECTIVE); return DtsPreTypes.ERROR; }
    ^ {PRE_PRAGMA}  / {PRE_END}     { yycall(PREPROCESSOR_DIRECTIVE); return DtsPreTypes.PRAGMA; }

<PREPROCESSOR_DIRECTIVE> {
    ([^\n] | (\\ \n))+              { return DtsPreTypes.CONTENT; }
    \n                              { yyreturn(); return DtsPreTypes.END; }
}

<BLOCK_COMMENT> {
    "*/"                            { yyreturn(); return DtsParserDefinition.BLOCK_COMMENT; }
    [^]                             { }
}

<REFERENCE> {
    {LABEL_NAME}                    { return DtsTypes.LABEL_NAME; }
    [^]                             { yyreturn(); yypushback(1); }
}

<YYINITIAL> {
    {HEADER_V1}                     { return DtsTypes.HEADER_V1; }
    "{"                             { return DtsTypes.BRACE_LEFT; }
    "}"                             { return DtsTypes.BRACE_RIGHT; }
    ";"                             { return DtsTypes.SEMICOLON; }
    "="                             { yybegin(VALUE); return DtsTypes.EQ; }
    "&"                             { yycall(REFERENCE); return DtsTypes.AMPERSAND; }
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
    "&"                             { yycall(REFERENCE); return DtsTypes.AMPERSAND; }
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

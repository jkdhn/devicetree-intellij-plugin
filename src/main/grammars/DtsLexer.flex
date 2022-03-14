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
WHITE_SPACE_ANY={EOL} | {WS}
WHITE_SPACE={WHITE_SPACE_ANY}+

INTEGER = ([0-9]+) | ("0b" [01]+) | ("0x" [0-9a-fA-F]+)

//STRING = \" ( [^\\\"\n] | (\\ [^]) )* \"
STRING = \" [^\"\n]* \"

IDENTIFIER = [0-9a-zA-Z#?@,._+-]+

PRE_DIRECTIVE = {WS}* "#" {WS}*
                (if|ifdef|ifndef|elif|else|endif|include|define|undef|line|error|pragma)
                ([^\\\n] | (\\ [^]))* \n

%%

<YYINITIAL> {
    "//" .* \R                      { return DtsParserDefinition.LINE_COMMENT; }
    "/*" ~"*/"                      { return DtsParserDefinition.BLOCK_COMMENT; }
    ^ {PRE_DIRECTIVE}               { return DtsParserDefinition.PREPROCESSOR_DIRECTIVE; }
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

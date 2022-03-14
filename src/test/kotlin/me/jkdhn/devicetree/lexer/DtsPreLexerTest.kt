package me.jkdhn.devicetree.lexer

import com.intellij.psi.TokenType
import me.jkdhn.devicetree.preprocessor.psi.PreTokenTypes
import me.jkdhn.devicetree.psi.DtsTypes
import kotlin.test.Test
import kotlin.test.assertEquals

internal class DtsPreLexerTest {
    @Test
    fun testDefine() {
        val lexer = createDtsPreLexer("#define NAME VALUE\n")
        assertEquals(Token(PreTokenTypes.HASH, "#"), lexer.consume())
        assertEquals(Token(PreTokenTypes.DIRECTIVE, "define"), lexer.consume())
        assertEquals(Token(TokenType.WHITE_SPACE, " "), lexer.consume())
        assertEquals(Token(PreTokenTypes.DEFINE_IDENTIFIER, "NAME"), lexer.consume())
        assertEquals(Token(TokenType.WHITE_SPACE, " "), lexer.consume())
        assertEquals(Token(PreTokenTypes.DEFINE_REPLACEMENT, "VALUE"), lexer.consume())
        assertEquals(Token(PreTokenTypes.END, "\n"), lexer.consume())
        assertEquals(null, lexer.consume())
    }

    @Test
    fun testDefineAndUse() {
        val lexer = createDtsPreLexer("#define NAME VALUE\nNAME {};")

        // Macro definition
        assertEquals(Token(PreTokenTypes.HASH, "#"), lexer.consume())
        assertEquals(Token(PreTokenTypes.DIRECTIVE, "define"), lexer.consume())
        assertEquals(Token(TokenType.WHITE_SPACE, " "), lexer.consume())
        assertEquals(Token(PreTokenTypes.DEFINE_IDENTIFIER, "NAME"), lexer.consume())
        assertEquals(Token(TokenType.WHITE_SPACE, " "), lexer.consume())
        assertEquals(Token(PreTokenTypes.DEFINE_REPLACEMENT, "VALUE"), lexer.consume())
        assertEquals(Token(PreTokenTypes.END, "\n"), lexer.consume())
        assertEquals(19, lexer.tokenStart)

        // Inserted macro content
        assertEquals(Token(DtsTypes.IDENTIFIER, "VALUE"), lexer.consume().unwrap())
        assertEquals(19, lexer.tokenStart) // same as above, should have 0 length

        // Macro reference
        assertEquals(Token(PreTokenTypes.MACRO, ""), lexer.consume())
        assertEquals(Token(PreTokenTypes.MACRO_NAME, "NAME"), lexer.consume())
        assertEquals(Token(PreTokenTypes.END, ""), lexer.consume())
        assertEquals(23, lexer.tokenStart)

        assertEquals(Token(TokenType.WHITE_SPACE, " "), lexer.consume())
        assertEquals(Token(DtsTypes.BRACE_LEFT, "{"), lexer.consume())
        assertEquals(Token(DtsTypes.BRACE_RIGHT, "}"), lexer.consume())
        assertEquals(Token(DtsTypes.SEMICOLON, ";"), lexer.consume())
        assertEquals(null, lexer.consume())
        assertEquals(27, lexer.tokenStart)
    }

    @Test
    fun testDefineAndUseWithParameters() {
        val lexer = createDtsPreLexer("#define NAME(p) PRE: p\nNAME(VALUE) {};")

        // Macro definition
        assertEquals(Token(PreTokenTypes.HASH, "#"), lexer.consume())
        assertEquals(Token(PreTokenTypes.DIRECTIVE, "define"), lexer.consume())
        assertEquals(Token(TokenType.WHITE_SPACE, " "), lexer.consume())
        assertEquals(Token(PreTokenTypes.DEFINE_IDENTIFIER, "NAME"), lexer.consume())
        assertEquals(Token(PreTokenTypes.LPAR, "("), lexer.consume())
        assertEquals(Token(PreTokenTypes.DEFINE_PARAMETER, "p"), lexer.consume())
        assertEquals(Token(PreTokenTypes.RPAR, ")"), lexer.consume())
        assertEquals(Token(TokenType.WHITE_SPACE, " "), lexer.consume())
        assertEquals(Token(PreTokenTypes.DEFINE_REPLACEMENT, "PRE: p"), lexer.consume())
        assertEquals(Token(PreTokenTypes.END, "\n"), lexer.consume())
        assertEquals(23, lexer.tokenStart)

        // Inserted macro content
        assertEquals(Token(DtsTypes.IDENTIFIER, "PRE"), lexer.consume().unwrap())
        assertEquals(Token(DtsTypes.COLON, ":"), lexer.consume().unwrap())
        assertEquals(Token(TokenType.WHITE_SPACE, " "), lexer.consume().unwrap())
        assertEquals(Token(DtsTypes.IDENTIFIER, "VALUE"), lexer.consume().unwrap())
        assertEquals(23, lexer.tokenStart) // same as above, should have 0 length

        // Macro reference
        assertEquals(Token(PreTokenTypes.MACRO, ""), lexer.consume())
        assertEquals(Token(PreTokenTypes.MACRO_NAME, "NAME"), lexer.consume())
        assertEquals(Token(PreTokenTypes.LPAR, "("), lexer.consume())
        assertEquals(Token(PreTokenTypes.MACRO_PARAMETER, "VALUE"), lexer.consume())
        assertEquals(Token(PreTokenTypes.RPAR, ")"), lexer.consume())
        assertEquals(Token(PreTokenTypes.END, " "), lexer.consume())
        assertEquals(35, lexer.tokenStart)

        assertEquals(Token(DtsTypes.BRACE_LEFT, "{"), lexer.consume())
        assertEquals(Token(DtsTypes.BRACE_RIGHT, "}"), lexer.consume())
        assertEquals(Token(DtsTypes.SEMICOLON, ";"), lexer.consume())
        assertEquals(null, lexer.consume())
        assertEquals(38, lexer.tokenStart)
    }
}

package me.jkdhn.devicetree.lexer

import com.intellij.psi.TokenType
import me.jkdhn.devicetree.preprocessor.psi.PreTokenTypes
import org.junit.Assert.assertEquals
import org.junit.Test

internal class PreLexerTest {
    @Test
    fun testDefine() {
        val lexer = PreLexer()
        lexer.start("#define NAME VALUE")
        assertEquals(Token(PreTokenTypes.HASH, "#"), lexer.consume())
        assertEquals(Token(PreTokenTypes.DIRECTIVE, "define"), lexer.consume())
        assertEquals(Token(TokenType.WHITE_SPACE, " "), lexer.consume())
        assertEquals(Token(PreTokenTypes.DEFINE_IDENTIFIER, "NAME"), lexer.consume())
        assertEquals(Token(TokenType.WHITE_SPACE, " "), lexer.consume())
        assertEquals(Token(PreTokenTypes.DEFINE_REPLACEMENT, "VALUE"), lexer.consume())
        assertEquals(null, lexer.consume())
    }

    @Test
    fun testDefineWithoutValue() {
        val lexer = PreLexer()
        lexer.start("#define NAME")
        assertEquals(Token(PreTokenTypes.HASH, "#"), lexer.consume())
        assertEquals(Token(PreTokenTypes.DIRECTIVE, "define"), lexer.consume())
        assertEquals(Token(TokenType.WHITE_SPACE, " "), lexer.consume())
        assertEquals(Token(PreTokenTypes.DEFINE_IDENTIFIER, "NAME"), lexer.consume())
        assertEquals(null, lexer.consume())
    }

    @Test
    fun testDefineWithoutValueWithTrailingSpace() {
        val lexer = PreLexer()
        lexer.start("#define NAME ")
        assertEquals(Token(PreTokenTypes.HASH, "#"), lexer.consume())
        assertEquals(Token(PreTokenTypes.DIRECTIVE, "define"), lexer.consume())
        assertEquals(Token(TokenType.WHITE_SPACE, " "), lexer.consume())
        assertEquals(Token(PreTokenTypes.DEFINE_IDENTIFIER, "NAME"), lexer.consume())
        assertEquals(Token(TokenType.WHITE_SPACE, " "), lexer.consume())
        assertEquals(null, lexer.consume())
    }
}

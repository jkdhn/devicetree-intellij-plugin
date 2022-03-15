package me.jkdhn.devicetree.lexer

import com.intellij.lexer.Lexer
import com.intellij.testFramework.LexerTestCase

internal class DtsPreLexerTest : LexerTestCase() {
    fun testDefine() {
        doTest("#define NAME 123\n")
    }

    fun testDefineAndUse() {
        doTest("#define NAME 123\n/ { k = <NAME>; };")
    }

    fun testDefineAndUseWithParameters() {
        doTest("#define MULTIPLY(a, b) ((a) * (b))\n/ { k = <MULTIPLY(3, 4)>; };")
    }

    fun testComplex() {
        doFileTest("dts")
    }

    override fun createLexer(): Lexer {
        return DtsPreLexer(null)
    }

    override fun getDirPath(): String {
        throw UnsupportedOperationException()
    }

    override fun getPathToTestDataFile(extension: String): String {
        return "src/test/testData/lexer/" + getTestName(false) + extension
    }
}

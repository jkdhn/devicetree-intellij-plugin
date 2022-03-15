package me.jkdhn.devicetree.lexer

import com.intellij.lexer.Lexer
import com.intellij.testFramework.LexerTestCase

internal class DtsPreLexerTest : LexerTestCase() {
    fun testDefine() {
        doTest("#define NAME VALUE\n")
    }

    fun testDefineAndUse() {
        doTest("#define NAME VALUE\nNAME {};")
    }

    fun testDefineAndUseWithParameters() {
        doTest("#define NAME(p) PRE: p\nNAME(VALUE) {};")
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

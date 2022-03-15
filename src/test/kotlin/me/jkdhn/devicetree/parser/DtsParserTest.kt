package me.jkdhn.devicetree.parser

import com.intellij.testFramework.ParsingTestCase

class DtsParserTest : ParsingTestCase("", "dts", DtsParserDefinition()) {
    fun testSimpleFile() {
        doTest(true)
    }

    override fun getTestDataPath(): String {
        return "src/test/testData/parser"
    }
}

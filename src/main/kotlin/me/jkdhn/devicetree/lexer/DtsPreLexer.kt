package me.jkdhn.devicetree.lexer

import com.intellij.lexer.Lexer
import com.intellij.lexer.LookAheadLexer
import me.jkdhn.devicetree.parser.DtsParserDefinition

class DtsPreLexer(base: Lexer) : LookAheadLexer(base) {
    override fun lookAhead(baseLexer: Lexer) {
        val type = baseLexer.tokenType
        if (type == DtsParserDefinition.PRE_INCLUDE || type == DtsParserDefinition.PRE_HEADER) {
            // Disguise preprocessor directives as comments
            // TODO Resolve includes
            advanceAs(baseLexer, DtsParserDefinition.LINE_COMMENT)
        } else {
            super.lookAhead(baseLexer)
        }
    }
}

package me.jkdhn.devicetree.lexer

import com.intellij.lang.ForeignLeafType
import com.intellij.lexer.Lexer
import com.intellij.lexer.LookAheadLexer
import com.intellij.psi.TokenType
import me.jkdhn.devicetree.parser.DtsParserDefinition
import me.jkdhn.devicetree.psi.DtsFile
import me.jkdhn.devicetree.psi.DtsIncludeType

class DtsPreLexer(private val file: DtsFile, base: Lexer) : LookAheadLexer(base) {
    override fun lookAhead(baseLexer: Lexer) {
        // Disguise preprocessor directives as comments
        when (baseLexer.tokenType) {
            DtsParserDefinition.PRE_INCLUDE -> {
                advanceAs(baseLexer, DtsParserDefinition.LINE_COMMENT)
                skipSpaces(baseLexer)
                if (baseLexer.tokenType == DtsParserDefinition.PRE_HEADER) {
                    handleInclude(baseLexer) // Attempt to insert included file
                    advanceAs(baseLexer, DtsParserDefinition.LINE_COMMENT)
                }
            }
            else -> {
                super.lookAhead(baseLexer)
            }
        }
    }

    private fun handleInclude(baseLexer: Lexer) {
        val text = baseLexer.tokenText
        val file = file.parent?.findFile(text.trim('<', '>', '"'))
        if (file is DtsFile) {
            addToken(baseLexer.tokenStart, DtsIncludeType(TokenType.WHITE_SPACE, "\n"))

            // Send the included file through a lexer
            val includeLexer = DtsPreLexer(file, DtsLexer())
            includeLexer.start(file.text)
            while (true) {
                val type = includeLexer.tokenType
                    ?: break // done
                when (type) {
                    is ForeignLeafType -> {
                        addToken(baseLexer.tokenStart, type)
                    }
                    else -> {
                        addToken(baseLexer.tokenStart, DtsIncludeType(type, includeLexer.tokenText))
                    }
                }
                includeLexer.advance()
            }
        }
    }

    private fun skipSpaces(baseLexer: Lexer) {
        while (baseLexer.tokenType in DtsParserDefinition.WHITE_SPACES) {
            advanceLexer(baseLexer)
        }
    }
}

package me.jkdhn.devicetree.lexer

import com.intellij.lang.ForeignLeafType
import com.intellij.lexer.Lexer
import com.intellij.lexer.LookAheadLexer
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import me.jkdhn.devicetree.parser.DtsParserDefinition.Companion.DISABLED_BRANCH
import me.jkdhn.devicetree.parser.DtsParserDefinition.Companion.PREPROCESSOR_DIRECTIVE
import me.jkdhn.devicetree.preprocessor.PreContext
import me.jkdhn.devicetree.psi.DtsFile
import me.jkdhn.devicetree.psi.DtsIncludeType
import me.jkdhn.devicetree.psi.DtsPreErrorType
import me.jkdhn.devicetree.psi.DtsPreErrorTypes
import me.jkdhn.devicetree.psi.DtsPreType
import me.jkdhn.devicetree.psi.DtsPreTypes

class DtsPreLexer(
    private val file: DtsFile?,
    private val context: PreContext = PreContext(),
    private val depth: Int = 0
) : LookAheadLexer(DtsLexer()) {
    override fun lookAhead(baseLexer: Lexer) {
        val type = baseLexer.tokenType
        if (type is DtsPreType) {
            handle(baseLexer, type)
        } else {
            super.lookAhead(baseLexer)
        }
    }

    private fun skipWhiteSpace(baseLexer: Lexer) {
        while (baseLexer.tokenType == TokenType.WHITE_SPACE) {
            advanceLexer(baseLexer)
        }
    }

    private fun handle(baseLexer: Lexer, type: DtsPreType) {
        advanceAs(baseLexer, PREPROCESSOR_DIRECTIVE)
        val content = if (baseLexer.tokenType == DtsPreTypes.CONTENT) baseLexer.tokenText.trim() else null
        handle(baseLexer, type, content)
    }

    private fun handle(baseLexer: Lexer, type: DtsPreType, content: String?) {
        when (type) {
            DtsPreTypes.IF -> {}
            DtsPreTypes.IFDEF -> handleIfdef(baseLexer, content)
            DtsPreTypes.IFNDEF -> handleIfndef(baseLexer, content)
            DtsPreTypes.ELIF -> {}
            DtsPreTypes.ELSE -> {}
            DtsPreTypes.ENDIF -> {}
            DtsPreTypes.INCLUDE -> handleInclude(baseLexer, content)
            DtsPreTypes.DEFINE -> handleDefine(baseLexer, content)
            DtsPreTypes.UNDEF -> handleUndefine(baseLexer, content)
            DtsPreTypes.LINE -> {}
            DtsPreTypes.ERROR -> {}
            DtsPreTypes.PRAGMA -> {}
            DtsPreTypes.CONTENT -> {}
            DtsPreTypes.END -> {}
        }
        if (baseLexer.tokenType == DtsPreTypes.CONTENT) {
            advanceAs(baseLexer, PREPROCESSOR_DIRECTIVE)
        }
        skipWhiteSpace(baseLexer)
        if (baseLexer.tokenType == DtsPreTypes.END) {
            advanceAs(baseLexer, TokenType.WHITE_SPACE)
        }
    }

    private fun skipUntil(baseLexer: Lexer, vararg until: IElementType) {
        while (true) {
            val type = baseLexer.tokenType
            if (type == null || type in until) {
                break
            }
            baseLexer.advance()
        }
    }

    private fun skipConditionalBranch(baseLexer: Lexer) {
        skipUntil(baseLexer, DtsPreTypes.ELSE, DtsPreTypes.ELIF, DtsPreTypes.ENDIF)
        addToken(baseLexer.tokenStart, DISABLED_BRANCH)
    }

    private fun error(baseLexer: Lexer, type: DtsPreErrorType) {
        advanceAs(baseLexer, type)
    }

    private fun handleIfdef(baseLexer: Lexer, content: String?) {
        if (content == null) {
            error(baseLexer, DtsPreErrorTypes.CONTENT_MISSING)
            return
        }

        advanceAs(baseLexer, PREPROCESSOR_DIRECTIVE)

        if (!context.isDefined(content)) {
            skipConditionalBranch(baseLexer)
        }
    }

    private fun handleIfndef(baseLexer: Lexer, content: String?) {
        if (content == null) {
            error(baseLexer, DtsPreErrorTypes.CONTENT_MISSING)
            return
        }

        advanceAs(baseLexer, PREPROCESSOR_DIRECTIVE)

        if (context.isDefined(content)) {
            skipConditionalBranch(baseLexer)
        }
    }

    private fun handleDefine(baseLexer: Lexer, content: String?) {
        if (content == null) {
            error(baseLexer, DtsPreErrorTypes.CONTENT_MISSING)
            return
        }

        val space = content.indexOf(' ')
        val key: String
        val value: String?
        if (space == -1) {
            key = content
            value = null
        } else {
            key = content.substring(0, space)
            value = content.substring(space + 1)
        }
        context.define(key, value)
    }

    private fun handleUndefine(baseLexer: Lexer, content: String?) {
        if (content == null) {
            error(baseLexer, DtsPreErrorTypes.CONTENT_MISSING)
            return
        }

        if (!context.undefine(content)) {
            error(baseLexer, DtsPreErrorTypes.UNDEFINE_NOT_FOUND)
        }
    }

    private fun handleInclude(baseLexer: Lexer, content: String?) {
        if (content == null) {
            error(baseLexer, DtsPreErrorTypes.CONTENT_MISSING)
            return
        }

        if (depth >= 15) {
            return
        }

        val file = file?.parent?.findFile(content.substring(1, content.length - 1))
        if (file !is DtsFile) {
            error(baseLexer, DtsPreErrorTypes.INCLUDE_NOT_FOUND)
            return
        }

        addToken(baseLexer.tokenStart, DtsIncludeType(TokenType.WHITE_SPACE, "\n"))

        // Send the included file through a lexer
        val includeLexer = DtsPreLexer(file, context, depth + 1)
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

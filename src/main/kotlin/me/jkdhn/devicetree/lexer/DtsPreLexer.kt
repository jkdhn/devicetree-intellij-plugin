package me.jkdhn.devicetree.lexer

import com.intellij.lexer.Lexer
import com.intellij.lexer.LookAheadLexer
import com.intellij.psi.PsiManager
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.util.indexing.IndexingDataKeys
import me.jkdhn.devicetree.DtsIncludeResolver
import me.jkdhn.devicetree.parser.DtsParserDefinition
import me.jkdhn.devicetree.preprocessor.PreContext
import me.jkdhn.devicetree.preprocessor.psi.PreTokenType
import me.jkdhn.devicetree.preprocessor.psi.PreTokenTypes
import me.jkdhn.devicetree.psi.DtsFile
import me.jkdhn.devicetree.psi.DtsIncludeType
import me.jkdhn.devicetree.psi.DtsMacroType
import me.jkdhn.devicetree.psi.DtsTypes

open class DtsPreLexer(
    private val file: DtsFile?,
    private val context: PreContext = PreContext(),
    private val depth: Int = 0
) : LookAheadLexer(DtsFlexLexer()) {
    override fun lookAhead(baseLexer: Lexer) {
        when (baseLexer.tokenType) {
            DtsParserDefinition.PREPROCESSOR_DIRECTIVE -> handle(baseLexer)
            DtsTypes.IDENTIFIER -> handleIdentifier(baseLexer)
            else -> super.lookAhead(baseLexer)
        }
    }

    private fun handle(baseLexer: Lexer) {
        val buffer = baseLexer.bufferSequence
        val start = baseLexer.tokenStart
        val end = baseLexer.tokenEnd

        val collector = TokenCollector()

        val subLexer = PreLexer()
        subLexer.start(buffer, start, end - 1) // ignore trailing newline

        if (subLexer.tokenType == TokenType.WHITE_SPACE) {
            collector.addToken(subLexer.tokenEnd, TokenType.WHITE_SPACE)
            subLexer.advance()
        }

        assert(subLexer.tokenType == PreTokenTypes.HASH)
        collector.addToken(subLexer.tokenEnd, PreTokenTypes.HASH)
        subLexer.advance()

        if (subLexer.tokenType == TokenType.WHITE_SPACE) {
            collector.addToken(subLexer.tokenEnd, TokenType.WHITE_SPACE)
            subLexer.advance()
        }

        assert(subLexer.tokenType == PreTokenTypes.DIRECTIVE)
        collector.addToken(subLexer.tokenEnd, PreTokenTypes.DIRECTIVE)
        val type = subLexer.tokenText
        subLexer.advance()

        val tokens = mutableListOf<Token>()
        while (true) {
            val tokenType = subLexer.tokenType
                ?: break
            if (tokenType is PreTokenType) {
                tokens += Token(tokenType, subLexer.tokenText)
            }
            collector.addToken(subLexer.tokenEnd, tokenType)
            subLexer.advance()
        }

        collector.addToken(end, PreTokenTypes.END)

        when (type) {
            "define" -> handleDefine(tokens)
            "include" -> handleInclude(start, tokens)
        }

        collector.apply(::addToken)
        baseLexer.advance()
    }

    private fun handleIdentifier(baseLexer: Lexer) {
        val nameStart = baseLexer.tokenStart
        val nameEnd = baseLexer.tokenEnd
        val text = baseLexer.tokenText
        val macro = context.getMacro(text)
        if (macro == null) {
            advanceLexer(baseLexer)
            return
        }

        val collector = TokenCollector()
        val parameterReplacements = mutableMapOf<String, String>()
        if (macro.parameters != null) {
            val position = baseLexer.currentPosition
            if (!collectParameters(baseLexer, parameterReplacements, macro.parameters, collector)) {
                baseLexer.restore(position)
                advanceLexer(baseLexer)
                return
            }
        }

        if (macro.replacement != null) {
            val subLexer = DtsFlexLexer()
            subLexer.start(macro.replacement)
            while (true) {
                val tokenType = subLexer.tokenType
                    ?: break
                var tokenText = subLexer.tokenText
                if (tokenType == DtsTypes.IDENTIFIER) {
                    val replacement = parameterReplacements[tokenText]
                    if (replacement != null) {
                        tokenText = replacement
                    }
                }
                addToken(nameStart, DtsMacroType(tokenType, tokenText))
                subLexer.advance()
            }
        }

        addToken(nameStart, PreTokenTypes.MACRO)
        addToken(nameEnd, PreTokenTypes.MACRO_NAME)
        collector.apply(::addToken)
        advanceAs(baseLexer, PreTokenTypes.END)
    }

    private fun collectParameters(
        baseLexer: Lexer, output: MutableMap<String, String>, names: List<String>, collector: TokenCollector
    ): Boolean {
        baseLexer.advance() // skip name

        if (baseLexer.tokenType != DtsTypes.PAR_LEFT) {
            return false
        }
        collector.addToken(baseLexer.tokenEnd, PreTokenTypes.LPAR)
        baseLexer.advance()

        var level = 0
        val currentText = StringBuilder()
        val parameters = mutableListOf<String>()
        while (level >= 0) {
            val type = baseLexer.tokenType
                ?: return false
            if (type == DtsTypes.PAR_LEFT) {
                level++
            } else if (type == DtsTypes.PAR_RIGHT) {
                level--
            }

            if (level == 0 && type == DtsTypes.COMMA || level < 0) {
                collector.addToken(baseLexer.tokenStart, PreTokenTypes.MACRO_PARAMETER)
                if (type == DtsTypes.COMMA) {
                    collector.addToken(baseLexer.tokenEnd, PreTokenTypes.COMMA)
                } else {
                    collector.addToken(baseLexer.tokenEnd, PreTokenTypes.RPAR)
                }
                parameters += currentText.toString()
                currentText.clear()
            } else {
                currentText.append(baseLexer.bufferSequence, baseLexer.tokenStart, baseLexer.tokenEnd)
            }
            baseLexer.advance()
        }

        if (parameters.size != names.size) {
            return false
        }

        for (index in names.indices) {
            output[names[index]] = parameters[index]
        }

        return true
    }

    private fun handleDefine(tokens: List<Token>) {
        var name: String? = null
        var value: String? = null
        var parameters: MutableList<String>? = null
        for (token in tokens) {
            when (token.type) {
                PreTokenTypes.DEFINE_IDENTIFIER -> name = token.text
                PreTokenTypes.DEFINE_REPLACEMENT -> value = token.text
                PreTokenTypes.LPAR -> parameters = mutableListOf()
                PreTokenTypes.DEFINE_PARAMETER -> parameters!! += token.text
            }
        }
        if (name == null) {
            return
        }
        context.define(name, PreContext.Macro(value, parameters))
    }

    private fun handleInclude(end: Int, tokens: List<Token>) {
        var header: String? = null
        for (token in tokens) {
            when (token.type) {
                PreTokenTypes.INCLUDE_HEADER -> header = token.text
            }
        }
        if (header == null) {
            return
        }
        handleInclude(end, header)
    }

    private fun handleInclude(end: Int, path: String) {
        if (depth >= 15) {
            return
        }
        val virtualFile = file?.originalFile?.virtualFile
        val resolved = DtsIncludeResolver.resolve(virtualFile, path)
        if (resolved != null) {
            val resolvedFile = PsiManager.getInstance(file!!.project).findFile(resolved) as? DtsFile
            if (resolvedFile != null) {
                handleInclude(end, resolvedFile)
            }
        }
    }

    private fun handleInclude(end: Int, resolvedFile: DtsFile) {
        val lexer = DtsPreLexer(resolvedFile, context, depth + 1)
        lexer.start(resolvedFile.text)
        while (true) {
            val type = lexer.tokenType
                ?: break
            addToken(end, DtsIncludeType(type, lexer.tokenSequence))
            lexer.advance()
        }
    }

    private class TokenCollector {
        private val tokens = mutableListOf<CollectedToken>()

        fun addToken(end: Int, type: IElementType) {
            tokens += CollectedToken(end, type)
        }

        fun apply(processor: (Int, IElementType) -> Unit) {
            for (token in tokens) {
                processor(token.end, token.type)
            }
        }

        private data class CollectedToken(val end: Int, val type: IElementType)
    }

    private data class Token(val type: PreTokenType, val text: String)
}

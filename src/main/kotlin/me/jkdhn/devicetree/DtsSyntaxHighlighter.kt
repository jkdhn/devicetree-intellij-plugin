package me.jkdhn.devicetree

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import me.jkdhn.devicetree.lexer.DtsHighlightingLexer
import me.jkdhn.devicetree.parser.DtsParserDefinition
import me.jkdhn.devicetree.preprocessor.psi.PreTokenType
import me.jkdhn.devicetree.psi.DtsTypes

class DtsSyntaxHighlighter : SyntaxHighlighterBase() {
    companion object {
        val HEADER = createTextAttributesKey("DTS_HEADER", DefaultLanguageHighlighterColors.METADATA)
        val PREPROCESSOR_DIRECTIVE = createTextAttributesKey("DTS_MACRO", DefaultLanguageHighlighterColors.METADATA)
        val DISABLED_BRANCH =
            createTextAttributesKey("DTS_DISABLED_BRANCH", DefaultLanguageHighlighterColors.LINE_COMMENT)
        val SEMICOLON = createTextAttributesKey("DTS_SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON)
        val LABEL = createTextAttributesKey("DTS_LABEL", DefaultLanguageHighlighterColors.LABEL)
        val NODE_NAME = createTextAttributesKey("DTS_NODE_NAME", DefaultLanguageHighlighterColors.IDENTIFIER)
        val PROPERTY_NAME = createTextAttributesKey("DTS_PROPERTY", DefaultLanguageHighlighterColors.IDENTIFIER)
        val BRACES = createTextAttributesKey("DTS_BRACES", DefaultLanguageHighlighterColors.BRACES)
        val LINE_COMMENT = createTextAttributesKey("DTS_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
        val BLOCK_COMMENT = createTextAttributesKey("DTS_BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT)
        val STRING = createTextAttributesKey("DTS_STRING", DefaultLanguageHighlighterColors.STRING)
        val NUMBER = createTextAttributesKey("DTS_NUMBER", DefaultLanguageHighlighterColors.NUMBER)

        val HEADER_KEYS = arrayOf(HEADER)
        val PREPROCESSOR_DIRECTIVE_KEYS = arrayOf(PREPROCESSOR_DIRECTIVE)
        val DISABLED_BRANCH_KEYS = arrayOf(DISABLED_BRANCH)
        val SEMICOLON_KEYS = arrayOf(SEMICOLON)
        val LABEL_KEYS = arrayOf(LABEL)
        val NODE_NAME_KEYS = arrayOf(NODE_NAME)
        val BRACES_KEYS = arrayOf(BRACES)
        val LINE_COMMENT_KEYS = arrayOf(LINE_COMMENT)
        val BLOCK_COMMENT_KEYS = arrayOf(BLOCK_COMMENT)
        val STRING_KEYS = arrayOf(STRING)
        val NUMBER_KEYS = arrayOf(NUMBER)
        val EMPTY_KEYS = arrayOf<TextAttributesKey>()
    }

    override fun getHighlightingLexer(): Lexer = DtsHighlightingLexer()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        return when (tokenType) {
            DtsTypes.HEADER_V1 -> HEADER_KEYS
            DtsTypes.SEMICOLON -> SEMICOLON_KEYS
            DtsTypes.LABEL_REFERENCE, DtsTypes.LABEL_DEFINITION -> LABEL_KEYS
            DtsTypes.IDENTIFIER -> NODE_NAME_KEYS
            DtsTypes.BRACE_LEFT, DtsTypes.BRACE_RIGHT -> BRACES_KEYS
            DtsParserDefinition.LINE_COMMENT -> LINE_COMMENT_KEYS
            DtsParserDefinition.BLOCK_COMMENT -> BLOCK_COMMENT_KEYS
            DtsParserDefinition.PREPROCESSOR_DIRECTIVE -> PREPROCESSOR_DIRECTIVE_KEYS
            DtsParserDefinition.DISABLED_BRANCH -> DISABLED_BRANCH_KEYS
            DtsTypes.STRING -> STRING_KEYS
            DtsTypes.INTEGER -> NUMBER_KEYS
            else -> EMPTY_KEYS
        }
    }
}

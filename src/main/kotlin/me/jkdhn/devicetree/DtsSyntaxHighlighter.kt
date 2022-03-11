package me.jkdhn.devicetree

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import me.jkdhn.devicetree.lexer.DtsLexerAdapter
import me.jkdhn.devicetree.psi.DtsTypes

class DtsSyntaxHighlighter : SyntaxHighlighterBase() {
    companion object {
        val HEADER = createTextAttributesKey("DTS_HEADER", DefaultLanguageHighlighterColors.METADATA)
        val MACRO = createTextAttributesKey("DTS_MACRO", DefaultLanguageHighlighterColors.METADATA)
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
        val MACRO_KEYS = arrayOf(MACRO)
        val SEMICOLON_KEYS = arrayOf(SEMICOLON)
        val LABEL_KEYS = arrayOf(LABEL)
        val NODE_NAME_KEYS = arrayOf(NODE_NAME)
        val PROPERTY_NAME_KEYS = arrayOf(PROPERTY_NAME)
        val BRACES_KEYS = arrayOf(BRACES)
        val LINE_COMMENT_KEYS = arrayOf(LINE_COMMENT)
        val BLOCK_COMMENT_KEYS = arrayOf(BLOCK_COMMENT)
        val STRING_KEYS = arrayOf(STRING)
        val NUMBER_KEYS = arrayOf(NUMBER)
        val EMPTY_KEYS = arrayOf<TextAttributesKey>()
    }

    override fun getHighlightingLexer() = DtsLexerAdapter()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        return when (tokenType) {
            DtsTypes.HEADER_V1 -> HEADER_KEYS
            DtsTypes.INCLUDE_START, DtsTypes.DIRECTIVE_START,
            DtsTypes.DIRECTIVE_VALUE, DtsTypes.DIRECTIVE_END -> MACRO_KEYS
            DtsTypes.SEMICOLON -> SEMICOLON_KEYS
            DtsTypes.LABEL -> LABEL_KEYS
            DtsTypes.NODE_NAME -> NODE_NAME_KEYS
            DtsTypes.PROPERTY_NAME -> PROPERTY_NAME_KEYS
            DtsTypes.BRACE_LEFT, DtsTypes.BRACE_RIGHT -> BRACES_KEYS
            DtsTypes.LINE_COMMENT -> LINE_COMMENT_KEYS
            DtsTypes.BLOCK_COMMENT -> BLOCK_COMMENT_KEYS
            DtsTypes.LITERAL_STRING -> STRING_KEYS
            DtsTypes.LITERAL_INT, DtsTypes.AT, DtsTypes.UNIT_ADDRESS -> NUMBER_KEYS
            else -> EMPTY_KEYS
        }
    }
}

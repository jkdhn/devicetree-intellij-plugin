package me.jkdhn.devicetree

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.psi.tree.IElementType
import me.jkdhn.devicetree.lexer.DtsPreLexer
import me.jkdhn.devicetree.parser.DtsParserDefinition
import me.jkdhn.devicetree.psi.DtsFile
import me.jkdhn.devicetree.psi.DtsPreErrorType
import me.jkdhn.devicetree.psi.DtsTypes

class DtsSyntaxHighlighter(
    private val project: Project?, private val virtualFile: VirtualFile?
) : SyntaxHighlighterBase() {
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
        val PROPERTY_NAME_KEYS = arrayOf(PROPERTY_NAME)
        val BRACES_KEYS = arrayOf(BRACES)
        val LINE_COMMENT_KEYS = arrayOf(LINE_COMMENT)
        val BLOCK_COMMENT_KEYS = arrayOf(BLOCK_COMMENT)
        val STRING_KEYS = arrayOf(STRING)
        val NUMBER_KEYS = arrayOf(NUMBER)
        val EMPTY_KEYS = arrayOf<TextAttributesKey>()
    }

    override fun getHighlightingLexer(): Lexer {
        var file: DtsFile? = null
        if (project != null && virtualFile != null) {
            file = PsiManager.getInstance(project).findFile(virtualFile) as? DtsFile
        }
        return DtsPreLexer(file)
    }

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        return when (tokenType) {
            DtsTypes.HEADER_V1 -> HEADER_KEYS
            DtsTypes.SEMICOLON -> SEMICOLON_KEYS
            DtsTypes.LABEL_NAME -> LABEL_KEYS
            DtsTypes.NODE_NAME -> NODE_NAME_KEYS
            DtsTypes.PROPERTY_NAME -> PROPERTY_NAME_KEYS
            DtsTypes.BRACE_LEFT, DtsTypes.BRACE_RIGHT -> BRACES_KEYS
            DtsParserDefinition.LINE_COMMENT -> LINE_COMMENT_KEYS
            DtsParserDefinition.BLOCK_COMMENT -> BLOCK_COMMENT_KEYS
            DtsParserDefinition.PREPROCESSOR_DIRECTIVE, is DtsPreErrorType -> PREPROCESSOR_DIRECTIVE_KEYS
            DtsParserDefinition.DISABLED_BRANCH -> DISABLED_BRANCH_KEYS
            DtsTypes.LITERAL_STRING -> STRING_KEYS
            DtsTypes.CELL_INT, DtsTypes.AT, DtsTypes.UNIT_ADDRESS -> NUMBER_KEYS
            else -> EMPTY_KEYS
        }
    }
}

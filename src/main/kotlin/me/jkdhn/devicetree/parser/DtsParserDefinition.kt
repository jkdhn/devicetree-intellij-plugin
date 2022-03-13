package me.jkdhn.devicetree.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import me.jkdhn.devicetree.lexer.DtsLexer
import me.jkdhn.devicetree.lexer.DtsPreLexer
import me.jkdhn.devicetree.psi.DtsElementFactory
import me.jkdhn.devicetree.psi.DtsFile
import me.jkdhn.devicetree.psi.DtsFileElementType
import me.jkdhn.devicetree.psi.DtsTokenType
import me.jkdhn.devicetree.psi.DtsTypes

class DtsParserDefinition : ParserDefinition {
    companion object {
        @JvmField
        val LINE_COMMENT = DtsTokenType("LINE_COMMENT")

        @JvmField
        val BLOCK_COMMENT = DtsTokenType("BLOCK_COMMENT")

        @JvmField
        val PRE_INCLUDE = DtsTokenType("#include")

        @JvmField
        val PRE_HEADER = DtsTokenType("<HEADER>")

        @JvmField
        val HEADER = DtsTokenType("[HEADER]")

        val WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE, HEADER)
        val COMMENTS = TokenSet.create(LINE_COMMENT, BLOCK_COMMENT)
        val STRINGS = TokenSet.create(DtsTypes.LITERAL_STRING)
        val FILE = DtsFileElementType()
    }

    override fun createLexer(project: Project): Lexer {
        val file = DtsElementFactory.createDummyFile(project, "")
        return DtsPreLexer(file, DtsLexer())
    }

    override fun getWhitespaceTokens() = WHITE_SPACES

    override fun getCommentTokens() = COMMENTS

    override fun getStringLiteralElements() = STRINGS

    override fun createParser(project: Project) = DtsParser()

    override fun getFileNodeType(): IFileElementType = FILE

    override fun createFile(viewProvider: FileViewProvider) = DtsFile(viewProvider)

    override fun spaceExistenceTypeBetweenTokens(left: ASTNode, right: ASTNode): ParserDefinition.SpaceRequirements {
        return ParserDefinition.SpaceRequirements.MAY
    }

    override fun createElement(node: ASTNode): PsiElement {
        return DtsTypes.Factory.createElement(node)
    }
}

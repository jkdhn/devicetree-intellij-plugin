package me.jkdhn.devicetree.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import me.jkdhn.devicetree.DtsLanguage
import me.jkdhn.devicetree.lexer.DtsLexerAdapter
import me.jkdhn.devicetree.psi.DtsFile
import me.jkdhn.devicetree.psi.DtsTypes

class DtsParserDefinition : ParserDefinition {
    private companion object {
        val WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE)
        val COMMENTS = TokenSet.create(DtsTypes.LINE_COMMENT, DtsTypes.BLOCK_COMMENT)
        val FILE = IFileElementType(DtsLanguage)
    }

    override fun createLexer(project: Project) = DtsLexerAdapter()

    override fun getWhitespaceTokens() = WHITE_SPACES

    override fun getCommentTokens() = COMMENTS

    override fun getStringLiteralElements(): TokenSet = TokenSet.EMPTY

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

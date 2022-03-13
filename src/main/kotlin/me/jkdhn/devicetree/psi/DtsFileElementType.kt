package me.jkdhn.devicetree.psi

import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilderFactory
import com.intellij.psi.tree.IFileElementType
import me.jkdhn.devicetree.DtsLanguage
import me.jkdhn.devicetree.lexer.DtsLexer
import me.jkdhn.devicetree.parser.DtsParser

class DtsFileElementType : IFileElementType("DTS_FILE", DtsLanguage) {
    override fun parseContents(chameleon: ASTNode): ASTNode {
        // Parse content in the context of this file
        val psi = chameleon.psi
        val file = psi.containingFile
        val lexer = DtsLexer(file as DtsFile)
        val builder = PsiBuilderFactory.getInstance().createBuilder(
            psi.project, chameleon, lexer, language, chameleon.chars
        )
        val parser = DtsParser()
        val node = parser.parse(chameleon.elementType, builder)
        return node.firstChildNode
    }
}

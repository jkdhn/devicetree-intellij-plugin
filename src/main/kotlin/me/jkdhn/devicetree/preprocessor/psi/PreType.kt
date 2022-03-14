package me.jkdhn.devicetree.preprocessor.psi

import com.intellij.lang.ASTNode
import com.intellij.psi.tree.IElementType
import me.jkdhn.devicetree.DtsLanguage

class PreType(
    name: String, private val factory: NodeFactory<PreElement>
) : IElementType(name, DtsLanguage) {
    fun createNode(node: ASTNode) = factory.createNode(node)
}

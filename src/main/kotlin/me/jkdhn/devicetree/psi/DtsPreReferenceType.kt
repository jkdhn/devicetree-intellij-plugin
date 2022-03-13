package me.jkdhn.devicetree.psi

import com.intellij.lang.ASTNode
import com.intellij.lang.TokenWrapper
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.ILeafElementType

class DtsPreReferenceType(
    private val type: IElementType, value: CharSequence
) : TokenWrapper(type, value), ILeafElementType {
    override fun createLeafNode(leafText: CharSequence): ASTNode {
        return LeafPsiElement(type, value)
    }

    override fun toString(): String {
        return "DtsPreReferenceType ($delegate)"
    }
}

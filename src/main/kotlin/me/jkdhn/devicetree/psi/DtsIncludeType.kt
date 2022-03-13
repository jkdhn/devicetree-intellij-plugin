package me.jkdhn.devicetree.psi

import com.intellij.lang.ASTNode
import com.intellij.lang.ForeignLeafType
import com.intellij.psi.tree.IElementType

class DtsIncludeType(delegate: IElementType, value: CharSequence) : ForeignLeafType(delegate, value) {
    override fun createLeafNode(leafText: CharSequence): ASTNode {
        return DtsIncludeNode(this, value)
    }
}

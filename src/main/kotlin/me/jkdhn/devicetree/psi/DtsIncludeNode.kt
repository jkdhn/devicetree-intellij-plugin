package me.jkdhn.devicetree.psi

import com.intellij.lang.ForeignLeafType
import com.intellij.openapi.util.TextRange
import com.intellij.psi.impl.source.tree.ForeignLeafPsiElement
import com.intellij.psi.impl.source.tree.TreeElement

class DtsIncludeNode(type: ForeignLeafType, text: CharSequence) : ForeignLeafPsiElement(type, text) {
    override fun getStartOffset(): Int {
        var result = 0
        var current: TreeElement = this
        while (current.treeParent != null) {
            result += current.startOffsetInParent
            current = current.treeParent!!
        }
        return result
    }

    override fun getTextRange(): TextRange {
        val start = startOffset
        return TextRange(start, start)
    }
}

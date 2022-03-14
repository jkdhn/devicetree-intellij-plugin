package me.jkdhn.devicetree.preprocessor.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.refactoring.suggested.startOffset
import me.jkdhn.devicetree.preprocessor.psi.PreDefine
import me.jkdhn.devicetree.preprocessor.psi.PreTokenTypes

class PreDefineImpl(node: ASTNode) : ASTWrapperPsiElement(node), PreDefine {
    override fun getNameIdentifier(): PsiElement? {
        return node.findChildByType(PreTokenTypes.DEFINE_IDENTIFIER)?.psi
    }

    override fun getTextOffset(): Int {
        return nameIdentifier?.startOffset ?: super.getTextOffset()
    }

    override fun hasParameters(): Boolean {
        return node.findChildByType(PreTokenTypes.LPAR) != null
    }

    override fun getParameters(): List<String> {
        val result = mutableListOf<String>()
        var child = node.firstChildNode
        while (child != null) {
            if (child.elementType == PreTokenTypes.DEFINE_PARAMETER) {
                result.add(child.text)
            }
            child = child.treeNext
        }
        return result
    }

    override fun getReplacement(): String? {
        return node.findChildByType(PreTokenTypes.DEFINE_REPLACEMENT)?.text
    }

    override fun getName(): String? {
        return nameIdentifier?.text
    }

    override fun setName(name: String): PsiElement {
        // TODO
        return this
    }
}

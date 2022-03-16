package me.jkdhn.devicetree.preprocessor.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.tree.ForeignLeafPsiElement
import com.intellij.psi.util.PsiTreeUtil
import me.jkdhn.devicetree.DtsMacroReference
import me.jkdhn.devicetree.preprocessor.psi.PreMacro
import me.jkdhn.devicetree.preprocessor.psi.PreTokenTypes

class PreMacroImpl(node: ASTNode) : ASTWrapperPsiElement(node), PreMacro {
    private fun getMacroName(): PsiElement? {
        return node.findChildByType(PreTokenTypes.MACRO_NAME)?.psi
    }

    override fun getReference(): PsiReference? {
        val range = getMacroName()?.textRangeInParent
            ?: return null
        return DtsMacroReference(this, range)
    }

    override fun getReplacement(): String? {
        var leaf = PsiTreeUtil.prevLeaf(this)
        val tokens = mutableListOf<String>()
        while (true) {
            if (leaf !is ForeignLeafPsiElement) {
                break
            }
            tokens += leaf.text
            leaf = PsiTreeUtil.prevLeaf(leaf)
        }
        tokens.reverse()
        return tokens.joinToString("")
    }
}

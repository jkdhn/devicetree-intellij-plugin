package me.jkdhn.devicetree.preprocessor.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
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
}

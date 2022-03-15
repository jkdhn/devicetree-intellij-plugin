package me.jkdhn.devicetree.preprocessor.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import me.jkdhn.devicetree.preprocessor.psi.PreIfdef

class PreIfdefImpl(node: ASTNode) : ASTWrapperPsiElement(node), PreIfdef {
}

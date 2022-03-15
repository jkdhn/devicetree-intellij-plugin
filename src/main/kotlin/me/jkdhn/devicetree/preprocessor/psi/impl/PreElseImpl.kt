package me.jkdhn.devicetree.preprocessor.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import me.jkdhn.devicetree.preprocessor.psi.PreElse

class PreElseImpl(node: ASTNode) : ASTWrapperPsiElement(node), PreElse {
}

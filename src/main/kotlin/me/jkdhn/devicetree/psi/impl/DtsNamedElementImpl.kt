package me.jkdhn.devicetree.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import me.jkdhn.devicetree.psi.DtsNamedElement

abstract class DtsNamedElementImpl(node: ASTNode) : ASTWrapperPsiElement(node), DtsNamedElement

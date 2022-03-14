package me.jkdhn.devicetree.preprocessor.psi

import com.intellij.lang.ASTNode

fun interface NodeFactory<T> {
    fun createNode(node: ASTNode): T
}

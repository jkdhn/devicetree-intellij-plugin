@file:JvmName("DtsPsiImplUtil")

package me.jkdhn.devicetree.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import me.jkdhn.devicetree.DtsLabelRef
import me.jkdhn.devicetree.psi.DtsElementFactory
import me.jkdhn.devicetree.psi.DtsLabelDefinition
import me.jkdhn.devicetree.psi.DtsLabelReference
import me.jkdhn.devicetree.psi.DtsTypes

private fun getIdentifier(element: PsiElement): ASTNode? =
    element.node.findChildByType(DtsTypes.IDENTIFIER)

fun getNameIdentifier(element: DtsLabelDefinition): PsiElement? =
    getIdentifier(element)?.psi

fun getName(element: DtsLabelDefinitionImpl): String? =
    getIdentifier(element)?.text

fun setName(element: DtsLabelDefinition, name: String): PsiElement {
    val oldName = getIdentifier(element)
    if (oldName != null) {
        val newName = DtsElementFactory.createIdentifier(element.project, name).node
        element.node.replaceChild(oldName, newName)
    }
    return element
}

fun getReference(element: DtsLabelReference): DtsLabelRef? {
    val name = getIdentifier(element)?.psi ?: return null
    return DtsLabelRef(element, name.textRangeInParent)
}

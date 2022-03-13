@file:JvmName("DtsPsiImplUtil")

package me.jkdhn.devicetree.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import me.jkdhn.devicetree.DtsLabelRef
import me.jkdhn.devicetree.DtsMacroReference
import me.jkdhn.devicetree.psi.DtsElementFactory
import me.jkdhn.devicetree.psi.DtsLabelDefinition
import me.jkdhn.devicetree.psi.DtsLabelReference
import me.jkdhn.devicetree.psi.DtsPreMarkerDefine
import me.jkdhn.devicetree.psi.DtsPreMarkerMacro
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

fun getReference(element: DtsPreMarkerMacro): DtsMacroReference? {
    val name = element.node.findChildByType(DtsTypes.PRE_MACRO_MARKER)?.psi ?: return null
    return DtsMacroReference(element, name.textRangeInParent)
}

fun getNameIdentifier(element: DtsPreMarkerDefine): PsiElement? =
    element.node.findChildByType(DtsTypes.PRE_DEFINE_MARKER)?.psi

fun getName(element: DtsPreMarkerDefine): String? =
    element.node.findChildByType(DtsTypes.PRE_DEFINE_MARKER)?.text?.trim()?.substringBefore(' ')

fun setName(element: DtsPreMarkerDefine, name: String): PsiElement {
//    val oldName = getIdentifier(element)
//    if (oldName != null) {
//        val newName = DtsElementFactory.createIdentifier(element.project, name).node
//        element.node.replaceChild(oldName, newName)
//    }
    return element
}

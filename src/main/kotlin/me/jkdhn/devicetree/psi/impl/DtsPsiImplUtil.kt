package me.jkdhn.devicetree.psi.impl

import com.intellij.psi.PsiElement
import me.jkdhn.devicetree.DtsReference
import me.jkdhn.devicetree.psi.DtsElementFactory
import me.jkdhn.devicetree.psi.DtsLabelDefinition
import me.jkdhn.devicetree.psi.DtsLabelReference
import me.jkdhn.devicetree.psi.DtsTypes

object DtsPsiImplUtil {
    private fun getLabelName(element: PsiElement) =
        element.node.findChildByType(DtsTypes.LABEL_NAME)

    @JvmStatic
    fun getName(element: DtsLabelDefinition): String? = getLabelName(element)?.text

    @JvmStatic
    fun setName(element: DtsLabelDefinition, name: String): PsiElement {
        val oldName = getLabelName(element)
        if (oldName != null) {
            val newName = DtsElementFactory.createLabelName(element.project, name).node
            element.node.replaceChild(oldName, newName)
        }
        return element
    }

    @JvmStatic
    fun getNameIdentifier(element: DtsLabelDefinition): PsiElement? = getLabelName(element)?.psi

    @JvmStatic
    fun getReference(reference: DtsLabelReference): DtsReference? {
        val name = getLabelName(reference)?.psi ?: return null
        return DtsReference(reference, name.textRangeInParent)
    }
}

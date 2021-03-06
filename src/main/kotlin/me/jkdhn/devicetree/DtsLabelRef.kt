package me.jkdhn.devicetree

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.util.PsiTreeUtil
import me.jkdhn.devicetree.psi.DtsElementFactory
import me.jkdhn.devicetree.psi.DtsLabelDefinition
import me.jkdhn.devicetree.psi.DtsLabelReference

class DtsLabelRef(
    private val element: DtsLabelReference, range: TextRange?
) : PsiReferenceBase<DtsLabelReference>(element, range) {
    override fun resolve(): PsiElement? {
        val name = rangeInElement.substring(element.text)
        val file = element.containingFile
        if (file != null) {
            val labels = PsiTreeUtil.findChildrenOfType(file, DtsLabelDefinition::class.java)
            for (label in labels) {
                if (label.name == name) {
                    return label
                }
            }
        }
        return null
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        val oldName = element.lastChild
        val newName = DtsElementFactory.createLabelName(element.project, newElementName)
        element.node.replaceChild(oldName.node, newName.node)
        return element
    }

    override fun toString(): String {
        return "DtsLabelReference -> ${rangeInElement.substring(element.text)}"
    }
}

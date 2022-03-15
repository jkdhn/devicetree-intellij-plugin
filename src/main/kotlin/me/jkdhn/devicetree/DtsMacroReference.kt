package me.jkdhn.devicetree

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.util.PsiTreeUtil
import me.jkdhn.devicetree.preprocessor.psi.PreDefine
import me.jkdhn.devicetree.preprocessor.psi.PreMacro
import me.jkdhn.devicetree.psi.DtsElementFactory

class DtsMacroReference(
    private val element: PreMacro, range: TextRange?
) : PsiReferenceBase<PreMacro>(element, range) {

    override fun resolve(): PsiElement? {
        val name = rangeInElement.substring(element.text)

        for (define in PsiTreeUtil.findChildrenOfType(element.containingFile, PreDefine::class.java)) {
            if (define.name == name) {
                return define
            }
        }

        return null
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        val oldName = element.firstChild
        val newName = DtsElementFactory.createIdentifier(element.project, newElementName)
        element.node.replaceChild(oldName.node, newName.node)
        return element
    }

    override fun toString(): String {
        return "DtsMacroReference -> ${rangeInElement.substring(element.text)}"
    }
}

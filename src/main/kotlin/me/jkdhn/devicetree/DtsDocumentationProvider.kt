package me.jkdhn.devicetree

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.psi.PsiElement
import me.jkdhn.devicetree.psi.DtsPreMarkerDefine

class DtsDocumentationProvider : AbstractDocumentationProvider() {
    override fun generateDoc(element: PsiElement, originalElement: PsiElement?): String? {
        return when (element) {
            is DtsPreMarkerDefine -> generateDefineDoc(element, originalElement)
            else -> super.generateDoc(element, originalElement)
        }
    }

    private fun generateDefineDoc(element: DtsPreMarkerDefine, originalElement: PsiElement?): String? {
        return element.text.trim().substringAfter(' ')
    }
}

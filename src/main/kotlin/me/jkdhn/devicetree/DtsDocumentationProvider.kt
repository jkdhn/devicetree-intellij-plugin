package me.jkdhn.devicetree

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiElement
import me.jkdhn.devicetree.preprocessor.psi.PreDefine
import me.jkdhn.devicetree.preprocessor.psi.PreMacro

class DtsDocumentationProvider : AbstractDocumentationProvider() {
    override fun generateDoc(element: PsiElement, originalElement: PsiElement?): String? {
        return when (element) {
            is PreDefine -> generateDefineDoc(element, originalElement)
            else -> super.generateDoc(element, originalElement)
        }
    }

    private fun generateDefineDoc(element: PreDefine, originalElement: PsiElement?): String {
        val result = StringBuilder()

        result.append("<h1>")
        result.append(element.name)
        if (element.hasParameters()) {
            result.append("(")
            var first = true
            for (parameter in element.getParameters()) {
                if (!first) {
                    result.append(", ")
                }
                first = false
                result.append(parameter)
            }
            result.append(")")
        }
        result.append("</h1>\n")

        val macro = originalElement?.parent as? PreMacro
        val replacement = if (macro != null) macro.getReplacement() else element.getReplacement()
        if (replacement != null) {
            result.append("Replacement:<br>\n")
            result.append(StringUtil.escapeXmlEntities(replacement))
        }

        return result.toString()
    }
}

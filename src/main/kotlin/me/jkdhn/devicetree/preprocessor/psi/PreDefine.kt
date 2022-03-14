package me.jkdhn.devicetree.preprocessor.psi

import com.intellij.psi.PsiNameIdentifierOwner

interface PreDefine : PreElement, PsiNameIdentifierOwner {
    fun hasParameters(): Boolean

    fun getParameters(): List<String>

    fun getReplacement(): String?
}

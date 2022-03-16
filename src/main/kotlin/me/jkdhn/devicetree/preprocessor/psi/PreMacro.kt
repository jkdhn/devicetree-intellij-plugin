package me.jkdhn.devicetree.preprocessor.psi

interface PreMacro : PreElement {
    fun getReplacement(): String?
}

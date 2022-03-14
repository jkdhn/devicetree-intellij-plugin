package me.jkdhn.devicetree.preprocessor

class PreContext {
    private val macros = mutableMapOf<String, Macro>()

    fun define(key: String, value: Macro) {
        macros[key] = value
    }

    fun undefine(key: String): Boolean {
        return macros.remove(key) != null
    }

    fun getMacro(key: String): Macro? {
        return macros[key]
    }

    data class Macro(val replacement: String?, val parameters: List<String>?)
}

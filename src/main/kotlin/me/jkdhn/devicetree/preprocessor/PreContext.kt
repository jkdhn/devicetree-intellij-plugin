package me.jkdhn.devicetree.preprocessor

class PreContext {
    private val macros = mutableMapOf<String, Macro>()
    private val conditional = mutableListOf<ConditionalScope>()

    fun define(key: String, value: Macro) {
        macros[key] = value
    }

    fun undefine(key: String): Boolean {
        return macros.remove(key) != null
    }

    fun getMacro(key: String): Macro? {
        return macros[key]
    }

    fun getScope() = conditional.lastOrNull()

    fun pushScope(scope: ConditionalScope) {
        conditional.add(scope)
    }

    fun replaceScope(scope: ConditionalScope) {
        conditional[conditional.lastIndex] = scope
    }

    fun popScope() {
        conditional.removeLast()
    }

    fun isInDisabledScope() = conditional.any { !it.result }

    data class Macro(val replacement: String?, val parameters: List<String>?)

    data class ConditionalScope(val result: Boolean, val finished: Boolean)
}

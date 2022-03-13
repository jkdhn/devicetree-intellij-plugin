package me.jkdhn.devicetree.preprocessor

class PreContext {
    private val defines = mutableMapOf<String, String?>()

    fun define(key: String, value: String?) {
        defines[key] = value
    }

    fun undefine(key: String): Boolean {
        val existed = defines.containsKey(key)
        defines.remove(key)
        return existed
    }

    fun isDefined(key: String): Boolean {
        return defines.containsKey(key)
    }

    fun getDefine(key: String): String? {
        return defines[key]
    }
}

package me.jkdhn.devicetree.preprocessor.psi

import me.jkdhn.devicetree.preprocessor.psi.impl.PreDefineImpl
import me.jkdhn.devicetree.preprocessor.psi.impl.PreIncludeImpl
import me.jkdhn.devicetree.preprocessor.psi.impl.PreMacroImpl

object PreTypes {
//    val IF = PreType("IF", ::PreIfImpl)
//    val IFDEF = PreType("IFDEF", ::PreIfdefImpl)
//    val IFNDEF = PreType("IFNDEF", ::PreIfndefImpl)
//    val ELIF = PreType("ELIF", ::PreElifImpl)
//    val ELSE = PreType("ELSE", ::PreElseImpl)
//    val ENDIF = PreType("ENDIF", ::PreEndifImpl)
    val INCLUDE = PreType("INCLUDE", ::PreIncludeImpl)
    val DEFINE = PreType("DEFINE", ::PreDefineImpl)
//    val UNDEF = PreType("UNDEF", ::PreUndefImpl)
//    val LINE = PreType("LINE", ::PreLineImpl)
//    val ERROR = PreType("ERROR", ::PreErrorImpl)
//    val PRAGMA = PreType("PRAGMA", ::PrePragmaImpl)
//    val CONTENT = PreType("CONTENT", ::PreContentImpl)
//    val END = PreType("END", ::PreEndImpl)
    val MACRO = PreType("MACRO", ::PreMacroImpl)

    fun getType(directive: String) = when (directive) {
        "include" -> INCLUDE
        "define" -> DEFINE
        else -> null
    }
}

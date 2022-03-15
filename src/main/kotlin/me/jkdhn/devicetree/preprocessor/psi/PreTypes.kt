package me.jkdhn.devicetree.preprocessor.psi

import me.jkdhn.devicetree.preprocessor.psi.impl.PreDefineImpl
import me.jkdhn.devicetree.preprocessor.psi.impl.PreElifImpl
import me.jkdhn.devicetree.preprocessor.psi.impl.PreElseImpl
import me.jkdhn.devicetree.preprocessor.psi.impl.PreEndifImpl
import me.jkdhn.devicetree.preprocessor.psi.impl.PreIfImpl
import me.jkdhn.devicetree.preprocessor.psi.impl.PreIfdefImpl
import me.jkdhn.devicetree.preprocessor.psi.impl.PreIfndefImpl
import me.jkdhn.devicetree.preprocessor.psi.impl.PreIncludeImpl
import me.jkdhn.devicetree.preprocessor.psi.impl.PreMacroImpl

object PreTypes {
    val IF = PreType("IF", ::PreIfImpl)
    val IFDEF = PreType("IFDEF", ::PreIfdefImpl)
    val IFNDEF = PreType("IFNDEF", ::PreIfndefImpl)
    val ELIF = PreType("ELIF", ::PreElifImpl)
    val ELSE = PreType("ELSE", ::PreElseImpl)
    val ENDIF = PreType("ENDIF", ::PreEndifImpl)
    val INCLUDE = PreType("INCLUDE", ::PreIncludeImpl)
    val DEFINE = PreType("DEFINE", ::PreDefineImpl)
//    val UNDEF = PreType("UNDEF", ::PreUndefImpl)
//    val LINE = PreType("LINE", ::PreLineImpl)
//    val ERROR = PreType("ERROR", ::PreErrorImpl)
//    val PRAGMA = PreType("PRAGMA", ::PrePragmaImpl)
    val MACRO = PreType("MACRO", ::PreMacroImpl)

    fun getType(directive: String) = when (directive) {
        "if" -> IF
        "ifdef" -> IFDEF
        "ifndef" -> IFNDEF
        "elif" -> ELIF
        "else" -> ELSE
        "endif" -> ENDIF
        "include" -> INCLUDE
        "define" -> DEFINE
        else -> null
    }
}

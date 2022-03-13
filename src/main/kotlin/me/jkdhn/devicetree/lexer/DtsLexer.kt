package me.jkdhn.devicetree.lexer

import com.intellij.lexer.DelegateLexer
import me.jkdhn.devicetree.psi.DtsFile

class DtsLexer(file: DtsFile?) : DelegateLexer(DtsPreLexer(file))

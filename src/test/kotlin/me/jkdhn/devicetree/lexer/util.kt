package me.jkdhn.devicetree.lexer

import com.intellij.lang.TokenWrapper
import com.intellij.lexer.Lexer
import com.intellij.psi.tree.IElementType

fun Lexer.consume(): Token? {
    val token = tokenType?.let { Token(it, tokenText) }
        ?: return null
    advance()
    return token
}

fun Token?.unwrap(): Token? {
    if (this == null) {
        return null
    }
    type as TokenWrapper
    return Token(type.delegate, type.value)
}

data class Token(val type: IElementType, val text: String)


fun createDtsPreLexer(text: String): Lexer {
    return object : DtsPreLexer(null) {
        private var pos = 0

        init {
            start(text)
        }

        override fun addToken(end: Int, type: IElementType?) {
            println("$pos to $end: $type")
            if (end < pos) {
                throw IllegalArgumentException()
            }
            pos = end
            super.addToken(end, type)
        }
    }
}

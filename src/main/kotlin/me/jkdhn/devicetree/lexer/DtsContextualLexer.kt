package me.jkdhn.devicetree.lexer

import com.intellij.lexer.Lexer
import com.intellij.lexer.MergeFunction
import com.intellij.lexer.MergingLexerAdapterBase
import com.intellij.psi.tree.IElementType
import me.jkdhn.devicetree.psi.DtsTypes

class DtsContextualLexer(base: Lexer) : MergingLexerAdapterBase(base) {
    override fun getMergeFunction(): MergeFunction {
        return DtsContextualMergeFunction
    }

    private object DtsContextualMergeFunction : MergeFunction {
        override fun merge(type: IElementType, originalLexer: Lexer): IElementType {
            if (type == DtsTypes.AMPERSAND) {
                if (originalLexer.tokenType == DtsTypes.IDENTIFIER) {
                    originalLexer.advance()
                    return DtsTypes.LABEL_REFERENCE
                }
            } else if (type == DtsTypes.IDENTIFIER) {
                if (originalLexer.tokenType == DtsTypes.COLON) {
                    originalLexer.advance()
                    return DtsTypes.LABEL_DEFINITION
                }
            }
            return type
        }
    }
}

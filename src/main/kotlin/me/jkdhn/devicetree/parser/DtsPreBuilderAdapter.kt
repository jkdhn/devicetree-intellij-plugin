package me.jkdhn.devicetree.parser

import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.lang.parser.GeneratedParserUtilBase
import com.intellij.psi.tree.IElementType
import me.jkdhn.devicetree.preprocessor.psi.PreTokenTypes
import me.jkdhn.devicetree.preprocessor.psi.PreTypes

class DtsPreBuilderAdapter(
    builder: PsiBuilder, state: GeneratedParserUtilBase.ErrorState, parser: PsiParser
) : DtsBuilderAdapter(builder, state, parser) {
    override fun advanceLexer() {
        parseMacros()
        super.advanceLexer()
    }

    override fun getTokenText(): String? {
        parseMacros()
        return super.getTokenText()
    }

    override fun getInternalTokenType(): IElementType? {
        parseMacros()
        return super.getInternalTokenType()
    }

    override fun eof(): Boolean {
        parseMacros()
        return super.eof()
    }

    override fun getCurrentOffset(): Int {
        parseMacros()
        return super.getCurrentOffset()
    }

    private fun parseMacros() {
        if (delegate.tokenType == PreTokenTypes.HASH) {
            val marker = mark()
            super.advanceLexer() // skip hash
            val type = delegate.tokenText?.let { PreTypes.getType(it) } // get type from directive
            while (delegate.tokenType != PreTokenTypes.END) {
                super.advanceLexer() // skip everything before end
            }
            // at end
            if (type != null) {
                marker.done(type)
            } else {
                marker.drop()
            }
            super.advanceLexer() // skip end
        } else if (delegate.tokenType == PreTokenTypes.MACRO) {
            val marker = mark()
            while (delegate.tokenType != PreTokenTypes.END) {
                super.advanceLexer() // skip everything before end
            }
            // at end
            marker.done(PreTypes.MACRO)
            super.advanceLexer() // skip end
        }
    }
}

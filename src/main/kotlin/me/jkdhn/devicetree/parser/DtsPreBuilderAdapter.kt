package me.jkdhn.devicetree.parser

import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.lang.parser.GeneratedParserUtilBase
import com.intellij.psi.tree.IElementType
import me.jkdhn.devicetree.psi.DtsPreReferenceType

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
        val type = delegate.tokenType
        if (type is DtsPreReferenceType) {
            // unwrap it this time
            val builder = DtsUnwrapBuilderAdapter(delegate, state, parser)
            val marker = DtsParserUtil.enter_section_(builder, 0, DtsParserUtil._NONE_, "PreMarker")
            val result = DtsParser.PreMarker(builder, 0)
            DtsParserUtil.exit_section_(builder, 0, marker, result, false, null)
        }
    }
}

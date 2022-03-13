package me.jkdhn.devicetree.parser

import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.lang.parser.GeneratedParserUtilBase
import com.intellij.psi.tree.IElementType
import me.jkdhn.devicetree.psi.DtsIncludeType

class DtsBuilderAdapter(
    builder: PsiBuilder, state: GeneratedParserUtilBase.ErrorState, parser: PsiParser
) : GeneratedParserUtilBase.Builder(builder, state, parser) {
    private fun unwrap(type: IElementType?): IElementType? {
        if (type is DtsIncludeType) {
            return type.delegate
        }
        return type
    }

    override fun getTokenType(): IElementType? {
        var type = unwrap(super.getTokenType())
        while (type in DtsParserDefinition.WHITE_SPACES || type in DtsParserDefinition.COMMENTS) {
            advanceLexer()
            type = unwrap(super.getTokenType())
        }
        return type
    }

    override fun getTokenText(): String? {
        val type = super.getTokenType()
        if (type is DtsIncludeType) {
            return type.value
        }
        return super.getTokenText()
    }

    override fun remapCurrentToken(type: IElementType) {
        val old = super.getTokenType()
        if (old is DtsIncludeType) {
            super.remapCurrentToken(DtsIncludeType(type, old.value))
        } else {
            super.remapCurrentToken(type)
        }
    }
}
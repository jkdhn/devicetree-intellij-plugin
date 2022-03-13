package me.jkdhn.devicetree.parser

import com.intellij.lang.ForeignLeafType
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.lang.parser.GeneratedParserUtilBase
import com.intellij.psi.tree.IElementType
import me.jkdhn.devicetree.psi.DtsIncludeType
import me.jkdhn.devicetree.psi.DtsPreReferenceType
import me.jkdhn.devicetree.psi.DtsMacroType

open class DtsBuilderAdapter(
    builder: PsiBuilder, state: GeneratedParserUtilBase.ErrorState, parser: PsiParser
) : GeneratedParserUtilBase.Builder(builder, state, parser) {
    private fun unwrap(type: IElementType?): IElementType? {
        if (type is ForeignLeafType) {
            return type.delegate
        }
        return type
    }

    override fun getTokenType(): IElementType? {
        var type = getInternalTokenType()
        while (type in DtsParserDefinition.WHITE_SPACES || type in DtsParserDefinition.COMMENTS) {
            advanceLexer()
            type = getInternalTokenType()
        }
        return type
    }

    protected open fun getInternalTokenType(): IElementType? {
        return unwrap(super.getTokenType())
    }

    override fun getTokenText(): String? {
        val type = super.getTokenType()
        if (type is ForeignLeafType) {
            return type.value
        }
        return super.getTokenText()
    }

    override fun remapCurrentToken(type: IElementType) {
        when (val old = super.getTokenType()) {
            is DtsIncludeType -> super.remapCurrentToken(DtsIncludeType(type, old.value))
            is DtsMacroType -> super.remapCurrentToken(DtsMacroType(type, old.value))
            is DtsPreReferenceType -> super.remapCurrentToken(DtsPreReferenceType(type, old.value))
            else -> super.remapCurrentToken(type)
        }
    }
}

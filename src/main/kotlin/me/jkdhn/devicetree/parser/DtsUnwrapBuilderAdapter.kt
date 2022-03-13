package me.jkdhn.devicetree.parser

import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.lang.TokenWrapper
import com.intellij.lang.parser.GeneratedParserUtilBase
import com.intellij.psi.tree.IElementType

class DtsUnwrapBuilderAdapter(
    builder: PsiBuilder, state: GeneratedParserUtilBase.ErrorState, parser: PsiParser
) : GeneratedParserUtilBase.Builder(builder, state, parser) {
    override fun getTokenType(): IElementType? {
        var type = super.getTokenType()
        while (type is TokenWrapper) {
            type = type.delegate
        }
        return type
    }
}

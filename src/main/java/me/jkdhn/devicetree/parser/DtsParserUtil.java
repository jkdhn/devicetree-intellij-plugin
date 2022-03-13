package me.jkdhn.devicetree.parser;


import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

public class DtsParserUtil extends GeneratedParserUtilBase {
    // Kotlin can't override/replace static methods, need to use Java for this class

    public static PsiBuilder adapt_builder_(IElementType root, PsiBuilder builder, PsiParser parser, TokenSet[] extendsSets) {
        ErrorState state = new ErrorState();
        ErrorState.initState(state, builder, root, extendsSets);
        return new DtsPreBuilderAdapter(builder, state, parser);
    }
}

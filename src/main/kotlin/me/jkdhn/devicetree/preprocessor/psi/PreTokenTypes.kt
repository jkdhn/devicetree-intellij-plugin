package me.jkdhn.devicetree.preprocessor.psi

object PreTokenTypes {
    @JvmField
    val HASH = PreTokenType("#")

    @JvmField
    val IDENTIFIER = PreTokenType("<identifier>")

    @JvmField
    val COMMA = PreTokenType(",")

    @JvmField
    val LPAR = PreTokenType("(")

    @JvmField
    val RPAR = PreTokenType(")")

    @JvmField
    val LT = PreTokenType("<")

    @JvmField
    val GT = PreTokenType(">")

    @JvmField
    val DQUOT = PreTokenType("\"")

    @JvmField
    val DIRECTIVE = PreTokenType("DIRECTIVE")

    @JvmField
    val INCLUDE_HEADER = PreTokenType("INCLUDE_HEADER")

    @JvmField
    val MACRO = PreTokenType("MACRO")

    @JvmField
    val MACRO_NAME = PreTokenType("MACRO_NAME")

    @JvmField
    val MACRO_PARAMETER = PreTokenType("MACRO_PARAMETER")

    @JvmField
    val DEFINE_IDENTIFIER = PreTokenType("DEFINE_IDENTIFIER")

    @JvmField
    val DEFINE_PARAMETER = PreTokenType("DEFINE_PARAMETER")

    @JvmField
    val DEFINE_REPLACEMENT = PreTokenType("DEFINE_REPLACEMENT")

    @JvmField
    val END = PreTokenType("END")
}

package me.jkdhn.devicetree.lexer

import com.intellij.lexer.FlexAdapter

class DtsLexerAdapter : FlexAdapter(DtsLexer(null))

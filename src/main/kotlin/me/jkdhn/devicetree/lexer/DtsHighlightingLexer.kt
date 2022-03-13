package me.jkdhn.devicetree.lexer

import com.intellij.lexer.DelegateLexer

class DtsHighlightingLexer : DelegateLexer(DtsContextualLexer(DtsFlexLexer()))

package me.jkdhn.devicetree.psi

import com.intellij.lang.ForeignLeafType
import com.intellij.psi.tree.IElementType

class DtsMacroType(type: IElementType, value: CharSequence) : ForeignLeafType(type, value)

package me.jkdhn.devicetree.psi

import com.intellij.psi.tree.IElementType
import me.jkdhn.devicetree.DtsLanguage
import org.jetbrains.annotations.NonNls

class DtsElementType(@NonNls debugName: String) : IElementType(debugName, DtsLanguage)

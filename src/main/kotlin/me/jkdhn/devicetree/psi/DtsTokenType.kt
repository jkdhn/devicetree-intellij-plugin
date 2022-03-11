package me.jkdhn.devicetree.psi

import com.intellij.psi.tree.IElementType
import me.jkdhn.devicetree.DtsLanguage
import org.jetbrains.annotations.NonNls

class DtsTokenType(@NonNls debugName: String) : IElementType(debugName, DtsLanguage) {
    override fun toString(): String {
        return "DtsTokenType." + super.toString()
    }
}

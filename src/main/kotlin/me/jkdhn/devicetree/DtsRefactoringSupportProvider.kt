package me.jkdhn.devicetree

import com.intellij.lang.refactoring.RefactoringSupportProvider
import com.intellij.psi.PsiElement
import me.jkdhn.devicetree.psi.DtsLabelDefinition

class DtsRefactoringSupportProvider : RefactoringSupportProvider() {
    override fun isMemberInplaceRenameAvailable(element: PsiElement, context: PsiElement?): Boolean {
        return element is DtsLabelDefinition
    }
}

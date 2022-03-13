package me.jkdhn.devicetree

import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.tree.TokenSet
import me.jkdhn.devicetree.lexer.DtsLexer
import me.jkdhn.devicetree.psi.DtsLabelDefinition
import me.jkdhn.devicetree.psi.DtsTypes
import me.jkdhn.devicetree.psi.impl.DtsLabelDefinitionImpl

class DtsFindUsagesProvider : FindUsagesProvider {
    override fun getWordsScanner(): WordsScanner {
        return DefaultWordsScanner(
            DtsLexer(),
            TokenSet.create(DtsTypes.LABEL_REFERENCE),
            TokenSet.EMPTY,
            TokenSet.EMPTY
        )
    }

    override fun canFindUsagesFor(element: PsiElement): Boolean {
        return element is PsiNamedElement
    }

    override fun getHelpId(psiElement: PsiElement): String? = null

    override fun getType(element: PsiElement): String {
        return when (element) {
            is DtsLabelDefinition -> "label"
            else -> element.toString()
        }
    }

    override fun getDescriptiveName(element: PsiElement): String {
        return when (element) {
            is DtsLabelDefinition -> element.name ?: "?"
            else -> ""
        }
    }

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String {
        return when (element) {
            is DtsLabelDefinitionImpl -> element.name ?: "null"
            else -> ""
        }
    }
}

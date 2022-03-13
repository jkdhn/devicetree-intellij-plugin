package me.jkdhn.devicetree.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.util.PsiTreeUtil
import me.jkdhn.devicetree.DtsFileType

object DtsElementFactory {
    fun createDummyFile(project: Project, text: String) =
        PsiFileFactory.getInstance(project).createFileFromText("dummy.dts", DtsFileType, text) as DtsFile

    private fun createLabelDefinition(project: Project, name: String): DtsLabelDefinition {
        val file = createDummyFile(project, "/dts-v1/; $name: / {};")
        return PsiTreeUtil.findChildOfType(file, DtsLabelDefinition::class.java)!!
    }

    fun createIdentifier(project: Project, name: String): PsiElement {
        return createLabelDefinition(project, name).node.findChildByType(DtsTypes.IDENTIFIER)!!.psi
    }
}

package me.jkdhn.devicetree.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider
import me.jkdhn.devicetree.DtsFileType
import me.jkdhn.devicetree.DtsLanguage

class DtsFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, DtsLanguage) {
    override fun getFileType() = DtsFileType

    override fun toString() = "DeviceTree source"
}

package me.jkdhn.devicetree

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

object DtsFileType : LanguageFileType(DtsLanguage) {
    override fun getName() = "Device Tree"

    override fun getDescription() = "Device Tree source file"

    override fun getDefaultExtension() = "dts"

    override fun getIcon(): Icon? = null
}

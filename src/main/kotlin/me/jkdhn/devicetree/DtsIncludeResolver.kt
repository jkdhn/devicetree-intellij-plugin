package me.jkdhn.devicetree

import com.intellij.openapi.vfs.VirtualFile

object DtsIncludeResolver {
    fun resolve(file: VirtualFile?, path: String): VirtualFile? {
        if (file == null) {
            return null
        }

        return file.parent?.findFileByRelativePath(path)
    }
}

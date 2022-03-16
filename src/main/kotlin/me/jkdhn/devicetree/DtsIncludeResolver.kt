package me.jkdhn.devicetree

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

object DtsIncludeResolver {
    fun resolve(project: Project?, file: VirtualFile?, path: String): VirtualFile? {
        if (project == null || file == null) {
            return null
        }

        val relative = file.parent?.findFileByRelativePath(path)
        if (relative != null) {
            return relative
        }

        val settings = DtsProjectSettingsState.getInstance(project)
        for (directory in settings.includeDirectories) {
            val dir = file.parent?.fileSystem?.findFileByPath(directory)
            if (dir != null) {
                val result = dir.findFileByRelativePath(path)
                if (result != null) {
                    return result
                }
            }
        }

        return null
    }
}

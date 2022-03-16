package me.jkdhn.devicetree

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import javax.swing.JComponent

class DtsProjectSettingsConfigurable(private val project: Project) : Configurable {
    private lateinit var component: DtsProjectSettingsComponent

    override fun createComponent(): JComponent? {
        component = DtsProjectSettingsComponent()
        return component.panel
    }

    override fun isModified(): Boolean {
        val settings = DtsProjectSettingsState.getInstance(project)
        return component.includeDirectories != settings.includeDirectories
    }

    override fun apply() {
        val settings = DtsProjectSettingsState.getInstance(project)
        settings.includeDirectories = component.includeDirectories
    }

    override fun reset() {
        val settings = DtsProjectSettingsState.getInstance(project)
        component.includeDirectories = settings.includeDirectories
    }

    override fun getDisplayName(): String {
        return "DeviceTree"
    }
}

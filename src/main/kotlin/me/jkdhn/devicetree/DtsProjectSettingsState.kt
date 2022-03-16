package me.jkdhn.devicetree

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.StoragePathMacros
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "me.jkdhn.devicetree.DtsProjectSettingsState",
    storages = [
        Storage(StoragePathMacros.WORKSPACE_FILE)
    ]
)
class DtsProjectSettingsState : PersistentStateComponent<DtsProjectSettingsState> {
    var includeDirectories = listOf<String>()

    companion object {
        fun getInstance(project: Project): DtsProjectSettingsState {
            return project.getService(DtsProjectSettingsState::class.java)
        }
    }

    override fun getState() = this

    override fun loadState(state: DtsProjectSettingsState) {
        XmlSerializerUtil.copyBean(state, this)
    }
}

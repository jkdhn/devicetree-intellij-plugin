package me.jkdhn.devicetree

import com.intellij.ui.components.JBTextArea
import com.intellij.util.ui.FormBuilder
import javax.swing.JPanel

class DtsProjectSettingsComponent {
    private val includeDirectoriesField = JBTextArea()

    val panel: JPanel = FormBuilder.createFormBuilder()
        .addLabeledComponentFillVertically("Include directories", includeDirectoriesField)
        .panel

    var includeDirectories: List<String>
        get() = includeDirectoriesField.text.split("\n").filter(String::isNotEmpty)
        set(value) {
            includeDirectoriesField.text = value.joinToString("\n")
        }
}

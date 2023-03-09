package com.github.cspeisman.chatgptintellijplugin.settings

import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel


class AppSettingsComponent {
    private val myMainPanel: JPanel
    private val myApiKey = JBTextField()
    private val myChatGptModel = JBTextField()

    init {
        myMainPanel = FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("ChatGpt Api Key:"), myApiKey, 1, false)
            .addLabeledComponent(JBLabel("ChatGpt Model: "), myChatGptModel, 1, false)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    val panel: JPanel
        get() = myMainPanel
    val preferredFocusedComponent: JComponent
        get() = myApiKey

    var apiKey: String?
        get() = myApiKey.text
        set(newText) {
            myApiKey.text = newText
        }
    var chatGptModel: String?
        get() = myChatGptModel.text
        set(newText) {
            myChatGptModel.text = newText
        }
}
